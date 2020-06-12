package com.luxosft.shapshot.exceptions;

import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BrokenFileException.class)
  public ResponseEntity<Object> handleCustomException(BrokenFileException e) {
    String error =
        e.getMessage();
    CustomErrorTemplate templateErrors =
        new CustomErrorTemplate(HttpStatus.BAD_REQUEST, String.valueOf(HttpStatus.BAD_REQUEST), error);
    return new ResponseEntity<>(
        templateErrors, new HttpHeaders(), templateErrors.getCode());

  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    String error =
        ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType())
            .getName();
    CustomErrorTemplate templateErrors =
        new CustomErrorTemplate(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
    return new ResponseEntity<>(
        templateErrors, new HttpHeaders(), templateErrors.getCode());
  }

}
