# Testing

To test a Spring Boot application that is a Kafka client, we need to be aware of different concepts.

## Black Box Testing

This will test the application with its external, observable behavior. It will connect to a running Kafka broker
(single or cluster) and send and receive messages.

### Requirements

In this case, we are dependent of a running Kafka broker, e.g.
 - as an external resource
 - as a locally running container, e.g. by using
   [Test Containers (Kafka Module)](https://java.testcontainers.org/modules/kafka/) or
   [Embedded Kafka (spring-kafka-test)](https://docs.spring.io/spring-kafka/reference/testing.html)

### Test Strategy

For a consumer, the test will send messages to a topic and verify that the messages are received and processed correctly.
For a producer, the test will trigger the application to send messages to a topic and verify that the messages can be
consumed from the topic with the correct content.

We could use the [Spring Kafka Test utilities](https://docs.spring.io/spring-kafka/reference/testing.html) or [Citrus](https://citrusframework.org/) to easily interact with the Kafka topic.

### Possibilities and Limitations

This will test...
- ✅ ... connecting to the Kafka broker using the configuration properties, incl. acknowledgment and exception handling.
- ✅ ... (de)serializing keys and payloads.
- ✅ ... performing schema validation.

This will NOT test...
- ❌ ... internal code, e.g. transaction management
- ❌ ... some exception scenarios


## Using Embedded Kafka

To avoid a local running Kafka broker, we could use the
[Embedded Kafka Broker](https://docs.spring.io/spring-kafka/reference/testing.html#embedded-kafka-annotation).
Spring Boot provides the `@EmbeddedKafka` annotation to start an in-memory Kafka broker for testing.

### Possibilities and Limitations

This will test...
- ✅ ... correct message emission.
- ✅ ... correct channel selection.
- ✅ ... correct key/value/structure.
- ✅ ... whether Jackson (or Avro, Protobuf, etc.) correctly (de)serializes the keys and payloads.

This will NOT test...
- ❌ ... whether the Kafka configuration works against a real broker.
- ❌ ... performance characteristics under load.

## Mocking

We could also mock the Spring beans that send and receive Kafka events. With this, we could completely disable the Kafka
connector, but this will have the most limited testing capabilities and is prone to maintainability issues.
