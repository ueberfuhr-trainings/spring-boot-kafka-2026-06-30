package de.sample.schulung.statistics.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerStatisticsService {

  private final CustomerStatisticsSink customerStatisticsSink;

  public CustomerStatistics getStatistics() {
    return customerStatisticsSink.getStatistics();
  }

}
