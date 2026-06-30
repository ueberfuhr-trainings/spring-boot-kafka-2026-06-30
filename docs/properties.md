# Spring Boot Properties for Kafka

We can find properties in the [Spring Boot Kafka documentation](https://docs.spring.io/spring-boot/appendix/application-properties/index.html#appendix.application-properties.integration).
For a comprehensive guide, see the [Spring for Apache Kafka reference](https://docs.spring.io/spring-kafka/reference/).

## Common Producer Properties

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
```

## Common Consumer Properties

```properties
spring.kafka.consumer.group-id=my-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.auto-offset-reset=earliest
```

When we want to know the details, we need to take a look into the source code of the following classes:

- `org.springframework.boot.autoconfigure.kafka.KafkaProperties`
- `org.apache.kafka.clients.producer.ProducerConfig`
- `org.apache.kafka.clients.consumer.ConsumerConfig`

For example, we can find the `group.id` property in the `ConsumerConfig` class:

```java
public class ConsumerConfig extends AbstractConfig {

  // ...

  /**
   * <code>group.id</code>
   */
  public static final String GROUP_ID_CONFIG = "group.id";

  /**
   * A unique string that identifies the consumer group this application belongs to.
   */
  private static final String GROUP_ID_DOC = "A unique string that identifies the consumer group...";

  // ...

}
```
