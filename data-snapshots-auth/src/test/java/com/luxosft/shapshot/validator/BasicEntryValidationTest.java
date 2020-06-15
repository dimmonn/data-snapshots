package com.luxosft.shapshot.validator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicEntryValidationTest {

  @Autowired
  private BasicEntryValidation basicEntryValidation;

  @Test
  public void isValid() {
    assertFalse(basicEntryValidation.isValid(new String[]{"1","name1","descr1","wrong date"}));
    assertTrue(basicEntryValidation.isValid(new String[]{"1","name1","descr1","31.07.2016 14:15 GMT+02:00"}));
    assertFalse(basicEntryValidation.isValid(new String[]{"wrong","name2","descr3","31.07.2016 14:15 GMT+02:00"}));
    assertFalse(basicEntryValidation.isValid(new String[]{"1","name2","","31.07.2016 14:15 GMT+02:00"}));
    assertFalse(basicEntryValidation.isValid(new String[]{"1","","descr3","31.07.2016 14:15 GMT+02:00"}));
    assertFalse(basicEntryValidation.isValid(new String[]{"1","","","31.07.2016 14:15 GMT+02:00"}));
  }
}