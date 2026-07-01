package de.sample.schulung.accounts.kafka;

import java.time.LocalDate;

public record CustomerRecord(
  String name,
  LocalDate birthdate,
  String state
) {
}
