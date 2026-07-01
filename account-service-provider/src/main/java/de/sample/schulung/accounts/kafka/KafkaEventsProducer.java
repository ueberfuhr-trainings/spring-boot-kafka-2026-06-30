package de.sample.schulung.accounts.kafka;

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
  private final KafkaTargetProperties target;

  private void send(CustomerEventRecord eventRecord) {
    kafkaTemplate.send(
      target.getTopic(),
      eventRecord.customerUuid(), // partition = message order
      eventRecord
    );
  }

  @EventListener
  public void onCustomerCreated(CustomerCreatedEvent event) {
    final var eventRecord = mapper.map(event);
    send(eventRecord);
  }

  @EventListener
  public void onCustomerReplaced(CustomerReplacedEvent event) {
    final var eventRecord = mapper.map(event);
    send(eventRecord);
  }

  @EventListener
  public void onCustomerDeleted(CustomerDeletedEvent event) {
    final var eventRecord = mapper.map(event);
    send(eventRecord);
  }

}
