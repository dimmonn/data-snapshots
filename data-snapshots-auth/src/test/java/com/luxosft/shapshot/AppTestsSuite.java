package com.luxosft.shapshot;

import com.luxosft.shapshot.controller.FileUploadControllerTest;
import com.luxosft.shapshot.controller.UserControllerTest;
import com.luxosft.shapshot.jwt.JWTAuthorizationFilterTest;
import com.luxosft.shapshot.validator.BasicDateValidatorTest;
import com.luxosft.shapshot.validator.BasicEntryValidationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BasicEntryValidationTest.class,
    FileUploadControllerTest.class,
    UserControllerTest.class,
    JWTAuthorizationFilterTest.class,
    BasicDateValidatorTest.class
})
public class AppTestsSuite {

}
