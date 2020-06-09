package com.luxosft.shapshot.auth.validator;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;

@Component
public class BasicEntryValidation implements EntryValidation {

  private final DateValidator dateValidator;

  public BasicEntryValidation(DateValidator dateValidator) {
    this.dateValidator = dateValidator;
  }

  @Override
  public boolean isValid(String[] entry) {
    return entry.length == 4 &&
        GenericValidator.isLong(entry[0]) &&
        !entry[1].isEmpty() &&
        !entry[2]
            .isEmpty() &&
        dateValidator.isValid(entry[3]);
  }

}
