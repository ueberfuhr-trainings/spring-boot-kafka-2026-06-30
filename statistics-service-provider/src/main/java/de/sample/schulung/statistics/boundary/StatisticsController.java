package de.sample.schulung.statistics.boundary;

import de.sample.schulung.statistics.domain.CustomerStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

  private final CustomerStatisticsService customerStatisticsService;
  private final CustomerStatisticsDtoMapper customerStatisticsDtoMapper;

  @GetMapping(
    value = "/customers",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public CustomerStatisticsDto getStatistics() {
    return this.customerStatisticsDtoMapper
      .map(
        this.customerStatisticsService
          .getStatistics()
      );
  }


}
