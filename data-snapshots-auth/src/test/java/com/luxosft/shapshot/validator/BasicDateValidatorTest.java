package com.luxosft.shapshot.validator;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicDateValidatorTest {

  @Autowired
  private BasicDateValidator basicDateValidator;
  @Autowired
  private SimpleDateFormat simpleDateFormat;

  @Test
  public void isValid() {
    assertFalse(basicDateValidator.isValid("wrong"));
    assertTrue(basicDateValidator.isValid("31.07.2016 14:15 GMT+02:00"));
    assertFalse(basicDateValidator.isValid("02/28/2019"));
    assertFalse(basicDateValidator.isValid("20190230"));
    assertFalse(basicDateValidator.isValid("2019-02-28"));
    assertFalse(basicDateValidator.isValid("2018-05-05T11:50:55"));
    assertFalse(basicDateValidator.isValid("2015-05-05T10:15:30+01:00[Europe/Paris]"));
    assertFalse(basicDateValidator.isValid("22-01-2015 10:15:55 AM"));
  }
}