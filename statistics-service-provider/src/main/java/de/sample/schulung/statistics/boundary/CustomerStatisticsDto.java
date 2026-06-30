package de.sample.schulung.statistics.boundary;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerStatisticsDto {

  private long count;
  private LocalDate earliestBirthdate;
  private LocalDate latestBirthdate;

}
