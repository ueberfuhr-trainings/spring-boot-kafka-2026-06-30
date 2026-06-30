package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, UUID> {

  @Query("SELECT count(c), min(c.dateOfBirth), max(c.dateOfBirth) FROM Customer c")
  Object[] getStatisticsAsArray();

  default CustomerStatistics getStatistics() {
    var statistics = (Object[]) this.getStatisticsAsArray()[0];
    var countObject = (Long) statistics[0];
    var count = countObject != null ? countObject : 0L;
    final var result = CustomerStatistics
      .builder()
      .count(count);
    if (count > 0) {
      result
        .earliestBirthdate((LocalDate) statistics[1])
        .latestBirthdate((LocalDate) statistics[2]);
    }
    return result.build();
  }

}