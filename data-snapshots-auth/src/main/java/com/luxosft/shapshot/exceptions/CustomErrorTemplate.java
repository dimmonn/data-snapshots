package com.luxosft.shapshot.exceptions;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomErrorTemplate {

  private HttpStatus code;
  private String message;
  private List<String> errors;

  public CustomErrorTemplate(HttpStatus code, String message, String error) {
    this.code = code;
    this.message = message;
    errors = Collections.singletonList(error);
  }

}
