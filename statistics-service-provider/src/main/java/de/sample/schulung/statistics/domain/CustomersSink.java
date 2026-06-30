package de.sample.schulung.statistics.domain;

import java.util.UUID;

public interface CustomersSink {

  void saveCustomer(Customer customer);

  void deleteCustomer(UUID uuid);

  long count();

}
