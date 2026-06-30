package de.sample.schulung.accounts.persistence;

import de.sample.schulung.accounts.domain.Customer.CustomerState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, UUID> {

  List<CustomerEntity> findByState(CustomerState state);

}
