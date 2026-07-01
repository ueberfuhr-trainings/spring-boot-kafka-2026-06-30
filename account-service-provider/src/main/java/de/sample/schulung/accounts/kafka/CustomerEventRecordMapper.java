package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.events.CustomerCreatedEvent;
import de.sample.schulung.accounts.domain.events.CustomerDeletedEvent;
import de.sample.schulung.accounts.domain.events.CustomerReplacedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerEventRecordMapper {

  default String mapState(Customer.CustomerState source) {
    return switch (source) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

  @Mapping(
    target = "birthdate",
    source = "dateOfBirth"
  )
  CustomerRecord map(Customer source);

  @Mapping(
    target = "eventType",
    constant = "created"
  )
  @Mapping(
    target = "customerUuid",
    source = "customer.uuid"
  )
  CustomerEventRecord map(CustomerCreatedEvent source);

  @Mapping(
    target = "eventType",
    constant = "replaced"
  )
  @Mapping(
    target = "customerUuid",
    source = "customer.uuid"
  )
  CustomerEventRecord map(CustomerReplacedEvent source);

  @Mapping(
    target = "eventType",
    constant = "deleted"
  )
  @Mapping(
    target = "customer",
    ignore = true
  )
  @Mapping(
    target = "customerUuid",
    source = "uuid"
  )
  CustomerEventRecord map(CustomerDeletedEvent source);


}
