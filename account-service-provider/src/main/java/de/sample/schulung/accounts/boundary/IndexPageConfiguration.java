package de.sample.schulung.accounts.boundary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * Lösung lt. Spring MVC
 *  - Implementiere einen WebMvcConfigurer
 *  - Packe diese Implementierung (Objekt) in den Context (Eimer)
 */
@Configuration // Spring Boot AutoConfiguration
public class IndexPageConfiguration {

  @Bean
    //Rückgabewert der Methode wird in den Eimer gepackt
  WebMvcConfigurer indexPageConfig() {
    return new WebMvcConfigurer() {
      @Override
      public void addViewControllers(
        @NonNull
        ViewControllerRegistry registry
      ) {
        registry
          .addViewController("/")
          .setViewName("redirect:/index.html");
      }
    };
  }

}
