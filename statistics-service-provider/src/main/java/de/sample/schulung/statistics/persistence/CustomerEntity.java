package de.sample.schulung.statistics.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "Customer")
@Table(name = "CUSTOMERS")
public class CustomerEntity {

  @NotNull
  @Id
  private UUID uuid;
  private LocalDate dateOfBirth;

}
