# JSON Schema & Schema Registry

> [!NOTE]
> This guide explains how to use JSON Schema with Spring Boot and Spring for Apache Kafka,
> including schema registration, message validation, and consumer deserialization.


## Introduction

Kafka messages can carry structured JSON payloads that evolve over time.
Schema registries ensure compatibility, validation, and governance of message formats.

Spring Boot supports schema-based serialization and deserialization through the
- [Apicurio Registry](https://www.apicur.io/registry/) or
- [Confluent Schema Registry](https://docs.confluent.io/platform/current/schema-registry/index.html).

## Example Domain Model

Let's assume we want to publish and consume customer-related events.

```java
public record CustomerEventRecord(
  String eventType,
  UUID customerUuid,
  CustomerRecord customer
) {
}

public record CustomerRecord(
  String name,
  LocalDate birthdate,
  String state
) {
}
```

An example JSON message looks like this:

```json
{
  "eventType": "created",
  "customerUuid": "3b6f5c52-8b3f-45b1-9f36-12f56f8a67a1",
  "customer": {
    "name": "Tom Mayer",
    "birthdate": "2000-12-01",
    "state": "active"
  }
}
```

## JSON Schema Definition

Below is a corresponding JSON Schema for the message payload, including validation rules:

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "type": "object",
  "properties": {
    "eventType": {
      "type": "string",
      "enum": [
        "created",
        "replaced",
        "deleted"
      ]
    },
    "customerUuid": {
      "type": "string",
      "format": "uuid"
    },
    "customer": {
      "type": "object",
      "properties": {
        "name": {
          "type": "string",
          "minLength": 3,
          "maxLength": 255
        },
        "birthdate": {
          "type": "string",
          "format": "date"
        },
        "state": {
          "type": "string",
          "enum": [
            "active",
            "locked",
            "disabled"
          ]
        }
      },
      "required": [
        "name",
        "birthdate",
        "state"
      ]
    }
  },
  "required": [
    "eventType",
    "customerUuid"
  ]
}
```

> [!NOTE]
> We could simply put the schema file in the producer project in `src/main/resources` where it is resolved
> at runtime during classpath scanning and registered automatically at the schema registry.

## Setting Up the Schema Registry

In this example we use [Apicurio Registry](https://www.apicur.io/registry/), which is compatible with Confluent Schema Registry APIs.

### Start Apicurio via Docker

```bash
docker run -it --rm -p 9080:8080 \
  -e REGISTRY_AUTH_ANONYMOUS_READ_ACCESS_ENABLED=true \
  -e REGISTRY_AUTH_ANONYMOUS_WRITE_ACCESS_ENABLED=true \
  quay.io/apicurio/apicurio-registry-mem:2.6.13.Final
```

The registry will be available at:
👉 http://localhost:9080/apis/registry/v2/

> [!CAUTION]
> We need a version `>= 2.5.x`, otherwise the API has another endpoint and does not match the defaults.
> Unfortunately, the `latest` tag is currently assigned to an older version, so we need to be careful.

We could upload the schema then by specifying an artifact id, e.g. `customer-events`.

### Spring Boot Configuration

Configure your Spring Boot application to use the registry and JSON Schema serialization:

```yaml
spring:
  kafka:
    producer:
      value-serializer: io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaSerializer
      properties:
        apicurio.registry.url: http://localhost:9080/apis/registry/v2
        apicurio.registry.auto-register: true
        apicurio.registry.artifact.schema.location: customer-events.schema.json
    consumer:
      value-deserializer: io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaDeserializer
      properties:
        apicurio.registry.url: http://localhost:9080/apis/registry/v2
        apicurio.registry.deserializer.value.return-class: de.sample.schulung.statistics.kafka.CustomerEventRecord
```

> [!NOTE]
> We specify the class used for deserialization in the `return-class` property.
> This is required for the deserializer to be able to deserialize the message.
> Otherwise, the deserializer reads it from the schema (element `javaType`), which does not seem to be
> a portable solution.

### Custom (De)Serializers

Unfortunately, there is a lack of integration of the Apicurio Kafka (de)serializers.
The Jackson `ObjectMapper` is created by the (de)serializers instead of using the Spring Boot configured instance.
This will run into problems when using Java's DateTime API for example.
Therefore, we need to do the integration part for ourselves by deriving custom classes
for serialization and deserialization:

```java
public class SpringJsonSchemaKafkaSerializer<T>
  extends JsonSchemaKafkaSerializer<T> {

  private final ObjectMapper objectMapper;

  public SpringJsonSchemaKafkaSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    super.configure(configs, isKey);
    this.setObjectMapper(objectMapper);
  }

}
```

```java
public class SpringJsonSchemaKafkaDeserializer<T>
  extends JsonSchemaKafkaDeserializer<T> {

  private final ObjectMapper objectMapper;

  public SpringJsonSchemaKafkaDeserializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    super.configure(configs, isKey);
    this.setObjectMapper(objectMapper);
  }

}
```
