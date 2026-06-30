package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.domain.CustomersService;
import de.sample.schulung.accounts.domain.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

  private final CustomersService service;
  private final CustomerDtoMapper mapper;

  @GetMapping(
    produces = MediaType.APPLICATION_JSON_VALUE)
  Stream<CustomerDto> getCustomers(
    @RequestParam(value = "state", required = false)
    String stateFilter
  ) {
    return (
      stateFilter == null
        ?
        this.service
          .getCustomers()
        :
        this.service
          .getCustomersByState(this.mapper.mapState(stateFilter))
    )
      .map(this.mapper::map);
  }

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<CustomerDto> createCustomer(
    @Valid @RequestBody CustomerDto body
  ) {
    var customer = this.mapper.map(body);
    service.createCustomer(customer);
    var uri = linkTo(
      methodOn(CustomersController.class)
        .findCustomerById(customer.getUuid())
    ).toUri();
    return ResponseEntity
      .created(uri)
      .body(this.mapper.map(customer));
  }

  @GetMapping(
    value = "/{uuid}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  CustomerDto findCustomerById(
    @PathVariable UUID uuid
  ) {
    return this.service
      .findCustomerById(uuid)
      .map(this.mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @PutMapping(
    value = "/{uuid}",
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void replaceCustomer(
    @PathVariable UUID uuid,
    @RequestBody CustomerDto customer) {
    customer.setUuid(uuid);
    service.replaceCustomer(this.mapper.map(customer));
  }

  @DeleteMapping("/{uuid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteCustomer(
    @PathVariable UUID uuid
  ) {
    this.service.deleteCustomer(uuid);
  }

}
