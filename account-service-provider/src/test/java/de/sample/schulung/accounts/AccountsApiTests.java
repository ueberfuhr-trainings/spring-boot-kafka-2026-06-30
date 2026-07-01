package de.sample.schulung.accounts;

import de.sample.schulung.accounts.kafka.AutoConfigureKafkaMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@AutoConfigureKafkaMock
class AccountsApiTests {

  @Autowired
  MockMvc mvc;

  @Test
  void shouldReturnCustomers() throws Exception {
    mvc.perform(
        get("/customers")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldNotReturnCustomersWithXml() throws Exception {
    mvc.perform(
        get("/customers")
          .accept(MediaType.APPLICATION_XML)
      )
      .andExpect(status().isNotAcceptable());
  }

  @Test
  void shouldCreateCustomer() throws Exception {
    var customerUrl = mvc.perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "name": "Tom Mayer",
              "birthdate": "1985-07-30",
              "state": "active"
            }
            """)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andExpect(header().exists("Location"))
      .andReturn()
      .getResponse()
      .getHeader("Location");

    assertThat(customerUrl)
      .isNotBlank();
    mvc.perform(
        get(customerUrl)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Tom Mayer"));
  }

  // POST /customers (Customer mit Namen, der nur aus 1 Buchstaben besteht) -> 400

  @Test
  void shouldNotCreateCustomerWithInvalidName() throws Exception {
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
  }

  @Test
  void shouldNotCreateCustomerWithInvalidState() throws Exception {
    mvc.perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "name": "Tom Mayer",
              "birthdate": "1985-07-30",
              "state": "gelbekatze"
            }
            """)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest());
  }

  @Nested
  class ExistingCustomerTests {

    String customerUrl;

    @BeforeEach
    void setup() throws Exception {
      customerUrl = mvc.perform(
          post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "name": "Tom Mayer",
                "birthdate": "1985-07-30",
                "state": "active"
              }
              """)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andReturn()
        .getResponse()
        .getHeader("Location");

      assertThat(customerUrl)
        .isNotBlank();
    }

    @Test
    void shouldReplaceCustomer() throws Exception {
      mvc.perform(
          put(customerUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "name": "Tom Schmidt",
                "birthdate": "1985-07-30",
                "state": "active"
              }
              """)
        )
        .andExpect(status().isNoContent());
      mvc.perform(
          get(customerUrl)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Tom Schmidt"));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
      // 1. Customer erstellen --> UUID / URL
      // (setup)
      // 2. Customer löschen -> 204 (wenn nochmal -> 404)
      mvc.perform(
          delete(customerUrl)
        )
        .andExpect(status().isNoContent());
      // 3. Customer abfragen -> 404
      mvc.perform(
          get(customerUrl)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    }

  }


}
