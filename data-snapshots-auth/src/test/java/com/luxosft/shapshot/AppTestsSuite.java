package com.luxosft.shapshot;

import com.luxosft.shapshot.auth.controller.FileUploadControllerTest;
import com.luxosft.shapshot.auth.controller.UserControllerTest;
import com.luxosft.shapshot.auth.jwt.JWTAuthorizationFilterTest;
import com.luxosft.shapshot.auth.validator.BasicDateValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    FileUploadControllerTest.class,
    UserControllerTest.class,
    JWTAuthorizationFilterTest.class,
    BasicDateValidatorTest.class
})
public class AppTestsSuite {

}
