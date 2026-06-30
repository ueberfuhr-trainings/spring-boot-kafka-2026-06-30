package de.sample.schulung.accounts.domain.sink;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.Customer.CustomerState;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface CustomersSink {

  Stream<Customer> getCustomers();

  Stream<Customer> getCustomersByState(CustomerState state);

  void createCustomer(Customer customer);

  Optional<Customer> findCustomerById(UUID uuid);

  void replaceCustomer(Customer customer);

  void deleteCustomer(UUID uuid);

  default boolean exists(UUID uuid) {
    return this.getCustomers()
      .anyMatch(c -> uuid.equals(c.getUuid()));
  }

  default long count() {
    return this.getCustomers()
      .count();
  }

}
