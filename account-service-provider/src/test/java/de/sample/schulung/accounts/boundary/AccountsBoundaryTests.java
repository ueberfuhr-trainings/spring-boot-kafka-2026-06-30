package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.domain.CustomersService;
import de.sample.schulung.accounts.domain.NotFoundException;
import de.sample.schulung.accounts.kafka.AutoConfigureKafkaMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@AutoConfigureKafkaMock
public class AccountsBoundaryTests {

  @Autowired
  MockMvc mvc;
  @MockitoBean // injiziere Mock (im Controller)
  CustomersService service;

  @Test
  void shouldReturnEmptyArrayOnGetCustomersWhenNoCustomersExist() throws Exception {

    when(service.getCustomers())
      .thenReturn(Stream.empty());
    mvc.perform(
        get("/customers")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().string("[]"));

  }

  @Test
  void shouldReturn404OnFindByIdIfCustomerNotFound() throws Exception {
    UUID customerId = UUID.randomUUID();
    when(service.findCustomerById(customerId))
      .thenReturn(Optional.empty());
    mvc.perform(
        get("/customers/{id}", customerId)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn404OnDeleteIfCustomerNotFound() throws Exception {
    UUID customerId = UUID.randomUUID();

    doThrow(NotFoundException.class)
      .when(service)
      .deleteCustomer(customerId);

    mvc.perform(
        delete("/customers/{id}", customerId)
      )
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204OnDeleteIfCustomerFound() throws Exception {
    UUID customerId = UUID.randomUUID();

    mvc.perform(
        delete("/customers/{id}", customerId)
      )
      .andExpect(status().isNoContent());

    verify(service).deleteCustomer(customerId);
  }

  @Test
  void shouldReturn400OnCreateInvalidCustomer() throws Exception {
    mvc.perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "name": "T",
              "birthdate": "1985-07-30",
              "state": "active"
            }
            """)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest());

    // Mockito.verify(service, Mockito.never()).createCustomer(ArgumentMatchers.any());
    verifyNoInteractions(service);
  }

}
