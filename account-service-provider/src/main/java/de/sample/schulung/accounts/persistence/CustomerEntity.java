package de.sample.schulung.accounts.persistence;

import de.sample.schulung.accounts.domain.Customer.CustomerState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "Customer")
@Table(name = "CUSTOMERS")
public class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;
  @Size(min = 3, max = 100)
  @NotNull
  private String name;
  @Column(name = "DATE_OF_BIRTH")
  private LocalDate dateOfBirth;
  @NotNull
  private CustomerState state = CustomerState.ACTIVE;

}
