package de.sample.schulung.accounts.persistence;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.Customer.CustomerState;
import de.sample.schulung.accounts.domain.NotFoundException;
import de.sample.schulung.accounts.domain.sink.CustomersSink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CustomersSinkJpaImpl implements CustomersSink {

  private final CustomerEntityRepository repo;
  private final CustomerEntityMapper mapper;

  @Override
  public Stream<Customer> getCustomers() {
    return repo.findAll()
      .stream()
      .map(mapper::map);
  }

  @Override
  public Stream<Customer> getCustomersByState(CustomerState state) {
    return repo.findByState(state)
      .stream()
      .map(mapper::map);
  }

  @Override
  public void createCustomer(Customer customer) {
    final var entity = mapper.map(customer);
    repo.save(entity);
    mapper.copy(entity, customer);
  }

  @Override
  public Optional<Customer> findCustomerById(UUID uuid) {
    return repo.findById(uuid)
      .map(mapper::map);
  }

  @Override
  public void replaceCustomer(Customer customer) {
    checkForExisting(customer.getUuid());
    final var entity = mapper.map(customer);
    repo.save(entity);
    mapper.copy(entity, customer);
  }

  @Override
  public void deleteCustomer(UUID uuid) {
    checkForExisting(uuid);
    repo.deleteById(uuid);
  }

  private void checkForExisting(UUID uuid) {
    if (!repo.existsById(uuid)) {
      throw new NotFoundException();
    }
  }

  @Override
  public boolean exists(UUID uuid) {
    return repo.existsById(uuid);
  }

  @Override
  public long count() {
    return repo.count();
  }
}
