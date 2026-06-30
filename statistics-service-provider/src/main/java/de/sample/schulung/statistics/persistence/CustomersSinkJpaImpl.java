package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.Customer;
import de.sample.schulung.statistics.domain.CustomersSink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomersSinkJpaImpl implements CustomersSink {

  private final CustomerEntityRepository repo;
  private final CustomerEntityMapper mapper;

  @Override
  public void saveCustomer(Customer customer) {
    var customerEntity = mapper.map(customer);
    repo.save(customerEntity);
  }

  @Override
  public void deleteCustomer(UUID uuid) {
    repo.deleteById(uuid);
  }

  @Override
  public long count() {
    return repo.count();
  }
}
