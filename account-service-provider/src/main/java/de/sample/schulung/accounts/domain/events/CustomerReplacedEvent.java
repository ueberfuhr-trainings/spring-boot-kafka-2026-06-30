package de.sample.schulung.accounts.domain.events;

import de.sample.schulung.accounts.domain.Customer;

public record CustomerReplacedEvent(
  Customer customer
) {
}
