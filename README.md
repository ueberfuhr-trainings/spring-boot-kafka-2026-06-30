# Spring Boot - Kafka

## Contents:

In this repository, we can find the following projects:

- [Account Service Provider](account-service-provider)
- [Statistics Service Provider](statistics-service-provider)

## Build and run the project

We use Maven to build the project.

```bash
# Build all
mvn clean package
# Run Account Service Provider
mvn -pl account-service-provider spring-boot:run
# Run Statistics Service Provider
mvn -pl statistics-service-provider spring-boot:run
```

# Documentation

Public documentation is available at

- Baeldung
  - [Kafka Basics](https://www.baeldung.com/apache-kafka)
- Spring
  - [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/)
  - [Spring Boot Kafka Properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html#appendix.application-properties.integration)
