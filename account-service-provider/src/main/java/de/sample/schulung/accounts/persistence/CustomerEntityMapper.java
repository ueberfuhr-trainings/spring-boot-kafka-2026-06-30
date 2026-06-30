package de.sample.schulung.accounts.persistence;

import de.sample.schulung.accounts.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

  CustomerEntity map(Customer source);

  Customer map(CustomerEntity source);

  void copy(CustomerEntity source, @MappingTarget Customer target);

}