package de.sample.schulung.accounts.domain;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  public enum CustomerState {
    ACTIVE, LOCKED, DISABLED
  }

  private UUID uuid;
  @Size(min = 3, max = 100)
  private String name;
  private LocalDate dateOfBirth;
  private CustomerState state;

}
