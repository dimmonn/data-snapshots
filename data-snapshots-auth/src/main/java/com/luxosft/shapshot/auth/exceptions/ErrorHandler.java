package com.luxosft.shapshot.auth.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(BrokenFileException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BrokenFileException handleCustomException(BrokenFileException e) {
    return e;
  }

  @ExceptionHandler(value = {EmptyResultDataAccessException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String constraintViolationException(EmptyResultDataAccessException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(value = {NumberFormatException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String internalServerError(NumberFormatException ex) {
    return ex.toString();
  }
}
