package com.luxosft.shapshot.exceptions;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BrokenFileException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleCustomException(BrokenFileException e) {
    return e.getMessage();
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<String> errors = new ArrayList<String>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    CustomErrorTemplate templateErrors =
        new CustomErrorTemplate(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    return handleExceptionInternal(
        ex, templateErrors, headers, templateErrors.getCode(), request);
  }
  @ExceptionHandler({ ConstraintViolationException.class })
  public ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    List<String> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + " " +
          violation.getPropertyPath() + ": " + violation.getMessage());
    }

    CustomErrorTemplate templateErrors =
        new CustomErrorTemplate(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    return handleExceptionInternal(
        ex, templateErrors, new HttpHeaders(), templateErrors.getCode(), request);
  }

  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    String error =
        ex.getName() + " should be of type " + ex.getRequiredType().getName();

    CustomErrorTemplate templateErrors =
        new CustomErrorTemplate(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
    return new ResponseEntity<>(
        templateErrors, new HttpHeaders(), templateErrors.getCode());
  }

}
