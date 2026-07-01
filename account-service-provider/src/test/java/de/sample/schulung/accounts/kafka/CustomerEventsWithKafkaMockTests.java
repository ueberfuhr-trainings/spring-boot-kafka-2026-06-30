package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.CustomersService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureKafkaMock
class CustomerEventsWithKafkaMockTests {

  @Autowired
  CustomersService customersService;
  @Autowired // Mock
  KafkaTemplate<UUID, CustomerEventRecord> kafkaTemplate;
  @Autowired
  KafkaTargetProperties target;

  @Test
  void shouldProduceMessageOnCustomerCreate() {
    var customer = new Customer();
    customer.setName("Tom");
    customer.setDateOfBirth(LocalDate.now().minusYears(20));
    customer.setState(Customer.CustomerState.ACTIVE);
    customersService.createCustomer(customer);

    verify(kafkaTemplate)
      .send(
        Mockito.eq(target.getTopic()),
        Mockito.any(),
        Mockito.any()
      );

  }

}
