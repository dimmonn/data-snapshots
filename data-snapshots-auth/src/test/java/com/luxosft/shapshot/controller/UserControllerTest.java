package com.luxosft.shapshot.controller;


import static org.junit.Assert.assertTrue;
import com.luxosft.shapshot.model.AuthUser;
import com.luxosft.shapshot.repository.AuthUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private AuthUserRepository authUserRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Before
  public void setUp() throws Exception {
    String body = "{\"username\":\"user\",\"password\":\"pass\"}";
    String contentAsString = mvc.perform(MockMvcRequestBuilders.post("/v1/users/sign-up")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body)).andReturn().getResponse().getContentAsString();
  }
  @After
  public void tearDown() {
    authUserRepository.deleteAll();
  }

  @Test
  public void signUp() throws Exception {
    AuthUser user = authUserRepository.findByUsername("user");
   // assertTrue(bCryptPasswordEncoder.matches("pass", user.getPassword()));
  }
}