package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.events.CustomerCreatedEvent;
import de.sample.schulung.accounts.domain.events.CustomerDeletedEvent;
import de.sample.schulung.accounts.domain.events.CustomerReplacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaEventsProducer {

  private final KafkaTemplate<UUID, CustomerEventRecord> kafkaTemplate;
  private final CustomerEventRecordMapper mapper;

  @EventListener
  public void handleCustomerCreated(CustomerCreatedEvent event){
    final var payload = mapper.map(event);
    kafkaTemplate.send(
      "customer-events",
      event.customer().getUuid(),
      payload
    );
  }

  @EventListener
  public void handleCustomerReplaced(CustomerReplacedEvent event){
    final var payload = mapper.map(event);
    kafkaTemplate.send(
      "customer-events",
      event.customer().getUuid(),
      payload
    );
  }

  @EventListener
  public void handleCustomerDeleted(CustomerDeletedEvent event){
    final var payload = mapper.map(event);
    kafkaTemplate.send(
      "customer-events",
      event.uuid(),
      payload
    );
  }

}
