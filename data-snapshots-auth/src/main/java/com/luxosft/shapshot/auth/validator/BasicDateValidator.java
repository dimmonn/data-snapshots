package com.luxosft.shapshot.auth.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Component;

@Component
public class BasicDateValidator implements DateValidator {

  private final SimpleDateFormat simpleDateFormat;

  public BasicDateValidator(
      SimpleDateFormat simpleDateFormat) {
    this.simpleDateFormat = simpleDateFormat;
  }

  @Override
  public boolean isValid(String dateStr) {
    try {
      simpleDateFormat.parse(dateStr);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }
}
