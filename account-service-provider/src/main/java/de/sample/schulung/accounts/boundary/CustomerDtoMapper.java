package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.domain.Customer;
import jakarta.validation.ValidationException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerDtoMapper {

  CustomerDto map(Customer source);

  Customer map(CustomerDto source);

  default String mapState(Customer.CustomerState source) {
    return switch (source) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

  default Customer.CustomerState mapState(String source) {
    return switch (source) {
      case "active" -> Customer.CustomerState.ACTIVE;
      case "locked" -> Customer.CustomerState.LOCKED;
      case "disabled" -> Customer.CustomerState.DISABLED;
      default -> throw new ValidationException();
    };
  }

}
