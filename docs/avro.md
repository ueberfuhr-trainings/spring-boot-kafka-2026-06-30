# Apache AVRO

## 🧩 What is Avro?

Avro is a data serialization system — originally developed by Apache as part of the Hadoop ecosystem.
It defines both:
1. A compact, fast, binary format for encoding data.
2. A schema language (usually in JSON) that defines the structure of that data.

## 🔹 In simple terms

Avro = binary JSON + schema

Think of Avro as a more efficient, structured version of JSON.
Instead of sending verbose field names and values like JSON does,
Avro sends a small binary payload that can be read using the schema.

## 🔹 Example

Here's a small Avro schema:

```json
{
  "type": "record",
  "name": "CustomerEventRecord",
  "namespace": "de.sample.schulung.accounts.kafka",
  "fields": [
    { "name": "eventType", "type": "string" },
    { "name": "customerUuid", "type": "string" },
    {
      "name": "customer",
      "type": {
        "type": "record",
        "name": "CustomerRecord",
        "fields": [
          { "name": "name", "type": "string" },
          { "name": "birthdate", "type": "string" },
          { "name": "state", "type": "string" }
        ]
      }
    }
  ]
}
```

This schema describes what fields exist, what their types are, and how they nest.

## 🔹 How Avro works in Kafka

Kafka messages can be encoded using Avro instead of JSON or plain strings.
When you use Avro with a Schema Registry (like Apicurio or Confluent Schema Registry):

1. The schema is stored and versioned in the registry.
2. The message payload sent to Kafka contains:
  - a small schema ID, and
  - the binary-encoded data according to that schema.

So, consumers don't need to know the schema in advance — they just fetch it by ID from the registry and decode the message.

## 🔹 Why use Avro?

| Feature                     | 	Avro                                   | 	JSON              |
|-----------------------------|-----------------------------------------|--------------------|
| Size                        | 	Binary (very small)	                   | Text (verbose)     |
| Speed                       | 	Fast to serialize/deserialize	         | Slower             |
| Schema evolution            | 	Built-in (backward/forward compatible) | 	Manual validation |
| Type safety                 | 	Strongly typed                         | 	Loose typing      |
| Schema Registry integration | 	Yes                                    | 	Optional          |

## ⚙️ Using Avro in Spring Boot

We can find details in the [Confluent documentation](https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-avro.html).
There is also an Integration for [Spring Cloud Stream](https://www.baeldung.com/spring-cloud-stream-kafka-avro-confluent).
