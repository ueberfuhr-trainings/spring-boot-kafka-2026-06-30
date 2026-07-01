package de.sample.schulung.statistics.kafka;

import de.sample.schulung.statistics.domain.CustomersService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Stubber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@SpringBootTest(
  properties = {
    "spring.kafka.consumer.auto-offset-reset=earliest",
    "spring.kafka.consumer.group-id=test-group"
  }
)
@AutoConfigureTestDatabase
@EmbeddedKafka(
  topics = "${application.kafka.customer-events.topic}"
)
public class CustomerEventConsumerTests {

  @Value("${application.kafka.customer-events.topic}")
  String topic;
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @MockitoBean
  CustomersService customersService;

  private static <T> Stubber doCountDownAndReturn(CountDownLatch latch, T result) {
    return doAnswer(invocation -> {
      latch.countDown();
      return null;
    });
  }

  private static Stubber doCountDown(CountDownLatch latch) {
    return doCountDownAndReturn(latch, null);
  }

  @SneakyThrows
  @Test
  void shouldConsumeCustomerCreatedEvent() {

    // use a countdown latch to wait for the service to be called
    final var latch = new CountDownLatch(1);
    doCountDown(latch)
      .when(customersService)
      .saveCustomer(any());

    var uuid = UUID.randomUUID();
    kafkaTemplate.send(
      topic,
      uuid.toString(),
      String.format(
        """
            {
              "eventType" : "created",
              "customerUuid" : "%s",
              "customer" : {
                "name" : "Tom Mayer",
                "birthdate" : [
                  2020,
                  4,
                  25
                ],
                "state" : "active"
              }
            }
          """,
        uuid
      )
    );

    assertThat(latch.await(3, TimeUnit.SECONDS))
      .isTrue();

    verify(customersService)
      .saveCustomer(argThat(
        customer ->
          uuid.equals(customer.getUuid())
            && LocalDate
            .of(2020, Month.APRIL, 25)
            .equals(customer.getDateOfBirth())
      ));


  }

}
