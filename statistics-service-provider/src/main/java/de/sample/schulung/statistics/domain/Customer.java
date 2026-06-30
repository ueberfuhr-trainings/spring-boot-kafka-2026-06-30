package de.sample.schulung.statistics.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Customer {

  @NotNull
  private UUID uuid;
  private LocalDate dateOfBirth;

}
