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

  private final KafkaTemplate<UUID, Customer> kafkaTemplate;

  @EventListener
  public void handleCustomerCreated(CustomerCreatedEvent event){
    System.out.println("Event!" + event);
    // TODO: Key+Payload sinnvoll gewählt?
    kafkaTemplate.send(
      "customer-events",
      event.customer().getUuid(),
      event.customer()
    );
  }

  @EventListener
  public void handleCustomerReplaced(CustomerReplacedEvent event){
    System.out.println("Event!" + event);
  }

  @EventListener
  public void handleCustomerDeleted(CustomerDeletedEvent event){
    System.out.println("Event!" + event);
  }

}
