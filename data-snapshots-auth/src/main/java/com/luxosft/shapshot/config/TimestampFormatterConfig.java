package com.luxosft.shapshot.config;

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimestampFormatterConfig {

  @Bean
  public SimpleDateFormat dateTimeFormatter() {
    return new SimpleDateFormat("dd.MM.yyyy HH:mm z", Locale.ENGLISH);
  }

}
