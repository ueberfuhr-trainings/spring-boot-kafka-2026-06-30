package de.sample.schulung.accounts.domain.sink;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

// TODO: Inject, if there isn't any other implementation available
// @Component
public class CustomersSinkInMemoryImpl implements CustomersSink {

  private final Map<UUID, Customer> customers = new HashMap<>();

  public Stream<Customer> getCustomers() {
    return customers
      .values()
      .stream();
  }

  public Stream<Customer> getCustomersByState(@NotNull Customer.CustomerState state) {
    return this.getCustomers()
      .filter(customer -> state.equals(customer.getState()));
  }

  public void createCustomer(@Valid Customer customer) {
    var uuid = UUID.randomUUID();
    customer.setUuid(uuid);
    this.customers.put(customer.getUuid(), customer);
  }

  public Optional<Customer> findCustomerById(@NotNull UUID uuid) {
    return Optional.ofNullable(
      this.customers.get(uuid)
    );
  }

  public void replaceCustomer(@Valid Customer customer) {
    if (this.exists(customer.getUuid())) {
      this.customers.put(customer.getUuid(), customer);
    } else {
      throw new NotFoundException();
    }
  }

  public void deleteCustomer(@NotNull UUID uuid) {
    if (this.exists(uuid)) {
      this.customers.remove(uuid);
    } else {
      throw new NotFoundException();
    }
  }

  public boolean exists(UUID uuid) {
    return this.customers.containsKey(uuid);
  }

  @Override
  public long count() {
    return this.customers.size();
  }
}
