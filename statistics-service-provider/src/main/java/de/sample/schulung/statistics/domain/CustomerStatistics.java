package de.sample.schulung.statistics.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CustomerStatistics {

  @Builder.Default
  private long count = 0;
  private LocalDate earliestBirthdate;
  private LocalDate latestBirthdate;

}
