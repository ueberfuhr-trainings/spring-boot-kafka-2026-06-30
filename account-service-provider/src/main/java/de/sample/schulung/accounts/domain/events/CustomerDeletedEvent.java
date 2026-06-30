package de.sample.schulung.accounts.domain.events;

import java.util.UUID;

public record CustomerDeletedEvent(
  UUID uuid
) {
}
