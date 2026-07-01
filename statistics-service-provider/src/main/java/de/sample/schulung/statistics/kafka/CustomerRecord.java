package de.sample.schulung.statistics.kafka;

import java.time.LocalDate;

public record CustomerRecord(
  String name,
  LocalDate birthdate,
  String state
) {
}
