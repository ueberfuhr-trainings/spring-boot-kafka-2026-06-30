package de.sample.schulung.accounts.kafka;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class KafkaTargetProperties {

  @NotNull
  @Length(min = 1)
  @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
  private String topic;

}
