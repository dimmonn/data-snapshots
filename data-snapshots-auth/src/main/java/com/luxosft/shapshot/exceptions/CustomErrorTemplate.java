package com.luxosft.shapshot.exceptions;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CustomErrorTemplate {

  private HttpStatus code;
  private String message;
  private List<String> errors;

  public CustomErrorTemplate(HttpStatus code, String message, String error) {
    this.code = code;
    this.message = message;
    errors = Arrays.asList(error);
  }

}
