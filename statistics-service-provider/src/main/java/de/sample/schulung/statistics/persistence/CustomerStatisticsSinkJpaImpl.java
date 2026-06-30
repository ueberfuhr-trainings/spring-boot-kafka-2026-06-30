package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import de.sample.schulung.statistics.domain.CustomerStatisticsSink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerStatisticsSinkJpaImpl implements CustomerStatisticsSink {

  private final CustomerEntityRepository repo;

  @Override
  public CustomerStatistics getStatistics() {
    return repo.getStatistics();
  }

}
