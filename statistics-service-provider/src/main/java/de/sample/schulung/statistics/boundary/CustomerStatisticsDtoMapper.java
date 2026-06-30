package de.sample.schulung.statistics.boundary;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerStatisticsDtoMapper {

  CustomerStatisticsDto map(CustomerStatistics source);

}
