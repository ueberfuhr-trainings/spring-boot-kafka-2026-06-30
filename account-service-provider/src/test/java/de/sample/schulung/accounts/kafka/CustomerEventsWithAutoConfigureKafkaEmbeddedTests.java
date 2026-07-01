package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.CustomersService;
import de.sample.schulung.accounts.kafka.AutoConfigureKafkaEmbedded.RecordsSupplier;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureKafkaEmbedded(
  partitions = 1,
  topics = {
    "customer-events"
  }
)
class CustomerEventsWithAutoConfigureKafkaEmbeddedTests {

  @Autowired
  CustomersService customersService;


  @Test
  void shouldProduceMessageOnCustomerCreate(
    RecordsSupplier<String, String> recordsSupplier
  ) {
    var customer = new Customer();
    customer.setName("Tom");
    customer.setDateOfBirth(LocalDate.now().minusYears(20));
    customer.setState(Customer.CustomerState.ACTIVE);
    customersService.createCustomer(customer);

    final var records = recordsSupplier
      .get();
    assertThat(records)
      .hasSize(1);
    var record = records
      .iterator()
      .next();
    assertThat(record)
      .returns("customer-events", from(ConsumerRecord::topic))
      .returns(customer.getUuid().toString(), from(ConsumerRecord::key));

  }

}
