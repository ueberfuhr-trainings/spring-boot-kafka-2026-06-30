package de.sample.schulung.accounts.shared.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method to get an event fired after method execution.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PublishEvent {

  /**
   * The event class. This class needs a constructor with the same parameters as the method.
   *
   * @return the event class
   */
  Class<?> value();

}
