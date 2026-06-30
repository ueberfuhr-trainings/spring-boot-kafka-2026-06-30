package de.sample.schulung.statistics.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Service
@RequiredArgsConstructor
public class CustomersService {

  private final CustomersSink customersSink;

  public void saveCustomer(Customer customer) {
    customersSink.saveCustomer(customer);
  }

  public void deleteCustomer(UUID uuid) {
    customersSink.deleteCustomer(uuid);
  }

  public long count() {
    return customersSink.count();
  }

}
