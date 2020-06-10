package com.luxosft.shapshot.auth.jwt;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JWTAuthorizationFilterTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void unauthenticatedAccess() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isForbidden());
  }

  @Test
  public void wrongCredantialsLoginTest() throws Exception {
    String body = "{\"username\":\"wrong\",\"password\":\"wrong\"}";
    mvc.perform(MockMvcRequestBuilders.post("/login")
        .content(body))
        .andExpect(status().isUnauthorized()).andReturn();
  }

  @Test
  public void signUpLoginAndUseTokenTest() throws Exception {
    String body = "{\"username\":\"user\",\"password\":\"pass\"}";
    mvc.perform(MockMvcRequestBuilders.post("/users/sign-up")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
        .andExpect(status().isCreated()).andReturn();
    ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/login")
        .content(body))
        .andExpect(status().isOk());
    String token = resultActions.andReturn().getResponse().getHeader("Authorization");
    mvc.perform(MockMvcRequestBuilders.get("/files/entries")
        .header("Authorization", token))
        .andExpect(status().isOk());
  }
}