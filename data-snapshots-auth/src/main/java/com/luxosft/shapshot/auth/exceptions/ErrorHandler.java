package com.luxosft.shapshot.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ErrorHandler {

  @ExceptionHandler(BrokenFileException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BrokenFileException handleCustomException(BrokenFileException e) {
    return e;
  }
}
