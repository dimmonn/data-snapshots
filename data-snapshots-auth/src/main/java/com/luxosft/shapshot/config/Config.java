package com.luxosft.shapshot.config;

import com.luxosft.shapshot.auth.validator.BasicDateValidator;
import com.luxosft.shapshot.auth.validator.DateValidator;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@Configuration
public class Config {

  final SimpleDateFormat simpleDateFormat;

  public Config(SimpleDateFormat simpleDateFormat) {
    this.simpleDateFormat = simpleDateFormat;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(
        Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization",
            "access-control-allow-origin"));
    configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers",
        ""
            + "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, "
            +
            "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean(name = "multipartResolver")
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(2 * 1024 * 1024);
    return multipartResolver;
  }

  @Bean
  public DateValidator dateValidator() {
    return new BasicDateValidator(simpleDateFormat);
  }
}
