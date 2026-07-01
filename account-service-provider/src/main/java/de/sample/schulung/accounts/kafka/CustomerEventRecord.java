package de.sample.schulung.accounts.kafka;

import java.util.UUID;

/*
 * JSON-Representation:
 * {
 *   "event_type": "created",
 *   "uuid": "12345678-1234-1234-1234-123456789012",
 *   "customer": {
 *     "name": "...",
 *     "birthdate": "..."
 *   }
 * }
 *
 */
public record CustomerEventRecord(
  String eventType,
  UUID customerUuid,
  CustomerRecord customer
) {
}
