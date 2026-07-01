package de.sample.schulung.accounts.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "application.kafka.customer-events")
  KafkaTargetProperties customerEventsTargetProperties() {
    return new KafkaTargetProperties();
  }

  // this snippet only for local tests
  @Bean
  NewTopic customerEventsTopic(KafkaTargetProperties target) {
    return TopicBuilder
      .name(target.getTopic())
      .build();
  }

}
