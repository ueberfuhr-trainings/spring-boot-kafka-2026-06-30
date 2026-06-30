package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

  CustomerEntity map(Customer customer);

}
