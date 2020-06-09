package com.luxosft.shapshot.basic;

import static org.assertj.core.api.Assertions.assertThat;

import com.luxosft.shapshot.auth.controller.FileUploadController;
import com.luxosft.shapshot.auth.controller.UserController;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

  @Autowired
  private FileUploadController fileUploadController;
  @Autowired
  private UserController userController;

  @Test
  public void contexLoads() {
    assertThat(fileUploadController).isNotNull();
    assertThat(userController).isNotNull();
  }
}