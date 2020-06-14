package com.luxosft.shapshot.jwt;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.luxosft.shapshot.repository.AuthUserRepository;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
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
  private Environment env;
  @Autowired
  private MockMvc mvc;
  private int expiration_time;
  private final String body = "{\"username\":\"user\",\"password\":\"pass\"}";
  private final String urlSignUp = "/v1/users/sign-up";
  private final String entriesUrl = "/v1/files/entries";
  private final String loginUrl = "/login";
  private String authorization;
  @Autowired
  private AuthUserRepository authUserRepository;

  @Before
  public void setUp() throws Exception {
    authorization = env.getProperty("auth_header");
    expiration_time = Integer.parseInt(env.getProperty("expiration_time"));
  }

  @After
  public void tearDown() throws Exception {
    authUserRepository.deleteAll();
  }

  @Test
  public void unauthenticatedAccess() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isForbidden());
  }

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void wrongCredantialsLoginTest() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post(loginUrl)
        .content(body))
        .andExpect(status().isUnauthorized()).andReturn();
  }

  @Test
  public void signUpLoginAndUseTokenTest() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post(urlSignUp)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
        .andExpect(status().isCreated()).andReturn();
    ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post(loginUrl)
        .content(body))
        .andExpect(status().isOk());
    String token = resultActions.andReturn().getResponse().getHeader(authorization);
    mvc.perform(MockMvcRequestBuilders.get(entriesUrl)
        .header(authorization, token))
        .andExpect(status().isOk());
  }

  @Test
  public void singUpLoginAndExpireTokenTest() throws Exception {
    exception.expect(TokenExpiredException.class);
    mvc.perform(MockMvcRequestBuilders.post(urlSignUp)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
        .andExpect(status().isCreated()).andReturn();
    ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post(loginUrl)
        .content(body))
        .andExpect(status().isOk());
    String token = resultActions.andReturn().getResponse().getHeader(authorization);
    TimeUnit.SECONDS.sleep(expiration_time);
    exception.expectMessage(
        "The Token has expired on " + DateUtils
            .addSeconds(new Date(), Math.negateExact(expiration_time))
            .toString() + ".");
    mvc.perform(MockMvcRequestBuilders.get(entriesUrl)
        .header(authorization, token))
        .andExpect(status().isOk());
  }
}

