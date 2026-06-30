package de.sample.schulung.accounts.domain;

import de.sample.schulung.accounts.domain.Customer.CustomerState;
import de.sample.schulung.accounts.domain.events.CustomerCreatedEvent;
import de.sample.schulung.accounts.domain.events.CustomerDeletedEvent;
import de.sample.schulung.accounts.domain.events.CustomerReplacedEvent;
import de.sample.schulung.accounts.domain.sink.CustomersSink;
import de.sample.schulung.accounts.shared.interceptors.PublishEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Validated
@Service
@RequiredArgsConstructor
public class CustomersService {

  private final CustomersSink sink;

  public Stream<Customer> getCustomers() {
    return sink.getCustomers();
  }

  public Stream<Customer> getCustomersByState(@NotNull CustomerState state) {
    return sink.getCustomersByState(state);
  }

  @PublishEvent(CustomerCreatedEvent.class)
  public void createCustomer(@Valid Customer customer) {
    sink.createCustomer(customer);
  }

  public Optional<Customer> findCustomerById(@NotNull UUID uuid) {
    return sink.findCustomerById(uuid);
  }

  @PublishEvent(CustomerReplacedEvent.class)
  public void replaceCustomer(@Valid Customer customer) {
    sink.replaceCustomer(customer);
  }

  @PublishEvent(CustomerDeletedEvent.class)
  public void deleteCustomer(@NotNull UUID uuid) {
    sink.deleteCustomer(uuid);
  }

  public boolean exists(UUID uuid) {
    return sink.exists(uuid);
  }

  public long count() {
    return sink.count();
  }

}
