# Kafka and Exception Handling in Spring Boot

> [!NOTE]
> This document describes best practices and techniques for handling exceptions in Kafka-based
> messaging systems using **Spring Boot** with **Spring for Apache Kafka**.

## Table of Contents

- [Introduction](#introduction)
- [Exception Handling Strategies for Producers](#exception-handling-strategies-for-producers)
  - [Acknowledgement Mode (Producer Side)](#acknowledgement-mode-producer-side)
  - [Transactional Outbox](#transactional-outbox)
- [Exception Handling Strategies for Consumers](#exception-handling-strategies-for-consumers)

## Introduction

Kafka is a distributed event streaming platform commonly used for reactive systems.
Message processing can fail due to temporary issues, validation errors, or downstream service failures. Proper exception handling ensures:

- Message reliability
- Fault tolerance
- System stability

Spring Boot integrates Kafka via **Spring for Apache Kafka**, supporting configurable strategies for error handling.


## Exception Handling Strategies for Producers

- **[Acknowledgement Mode (Producer Side)](#acknowledgement-mode-producer-side):** Control when a message is considered successfully sent.
- **[Transactional Outbox](#transactional-outbox):** Ensure atomicity between database operations and Kafka events.


### Acknowledgement Mode (Producer Side)

When producing messages in Kafka, the acknowledgement mode determines how many Kafka brokers must confirm the receipt of a message before the producer considers it _"successfully sent"_.

This directly affects:
- Message delivery guarantees
- Performance and latency
- Fault tolerance

We can configure the acknowledgement mode in `application.yml`:

```yaml
spring:
  kafka:
    producer:
      acks: all
```

These are the available options:

| Option          | Description                                                        | Reliability | Latency    |
|-----------------|--------------------------------------------------------------------|-------------|------------|
| `all` (or `-1`) | Wait for all in-sync replicas (ISRs) to acknowledge                | ✅ highest   | 🐢 slowest |
| `1`             | (Default) Leader broker acknowledges receipt before replication.   | ⚠️ partial  | ⚡️ fast    |
| `0`             | Fire and forget — producer doesn't wait for broker acknowledgment. | ❌ none      | ⚡⚡ fastest |

> [!NOTE]
> See the [Spring for Apache Kafka - Sending Messages](https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html) guide for more details.


### Transactional Outbox

The Transactional Outbox pattern solves a classic problem:

> "How do I ensure that when I update my database, the corresponding Kafka event is also sent — exactly once —
> even if my service crashes halfway through?"

This pattern ensures atomicity between database changes and message publishing, without relying on
distributed transactions (which are complex and slow).

#### 💡 The Problem

Imagine this code (a naive approach):

```java
@Transactional
public void createCustomer(Customer customer) {
  customerRepository.save(customer);
  kafkaTemplate.send("customers", customerEvent); // <-- may fail!
}
```

If Kafka is temporarily unavailable, the customer is saved in the DB,
but the event is lost — the system becomes inconsistent.

#### 🧩 The Solution

Instead of sending directly to Kafka in the same transaction,
we store the event in an "outbox" table inside the same database transaction as our business data.

Later, a background process, sidecar container or any other poller reads unsent events from this table,
sends them to Kafka, and then marks them as sent.

#### ✅ Guarantees

- **Atomicity**: Database change and event save happen in one transaction.
- **Reliability**: If Kafka send fails, the event remains in DB until retried.
- **Idempotency**: The poller can safely retry sending without duplicates (with `enable.idempotence`=true on Kafka producer).

> [!NOTE]
> Spring Boot does not directly support the Transactional Outbox pattern, but it's trivial to implement manually.
> An alternative could be the use of [Debezium](https://debezium.io/documentation/reference/stable/integrations/outbox.html),
> which listens to the database transaction log (e.g. MySQL binlog, PostgreSQL WAL, Oracle redo log) and turns
> every data change into a Kafka event automatically.


## Exception Handling Strategies for Consumers

> [!NOTE]
> We can find details in the [Spring for Apache Kafka - Error Handling](https://docs.spring.io/spring-kafka/reference/kafka/annotation-error-handling.html)
> documentation.
