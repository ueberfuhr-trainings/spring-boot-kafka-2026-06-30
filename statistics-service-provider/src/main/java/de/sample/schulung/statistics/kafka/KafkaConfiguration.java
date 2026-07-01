package de.sample.schulung.statistics.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "application.kafka.customer-events")
  KafkaTargetProperties customerEventsTargetProperties() {
    return new KafkaTargetProperties();
  }

}
