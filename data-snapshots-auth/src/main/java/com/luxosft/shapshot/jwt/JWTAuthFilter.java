
package com.luxosft.shapshot.jwt;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxosft.shapshot.model.AuthUser;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

import static com.luxosft.shapshot.jwt.SecurityConstraints.EXPIRATION_TIME;
import static com.luxosft.shapshot.jwt.SecurityConstraints.HEADER_STRING;
import static com.luxosft.shapshot.jwt.SecurityConstraints.SECRET;
import static com.luxosft.shapshot.jwt.SecurityConstraints.TOKEN_PREFIX;

public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JWTAuthFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
      HttpServletResponse res) throws AuthenticationException {

    AuthUser creds = new ObjectMapper()
        .readValue(req.getInputStream(), AuthUser.class);

    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            creds.getUsername(),
            creds.getPassword(),
            new ArrayList<>())
    );

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain,
      Authentication auth) {
    String token = JWT.create()
        .withSubject(((User) auth.getPrincipal()).getUsername())
        .withExpiresAt(
            new Date(System.currentTimeMillis() + Long.parseLong(EXPIRATION_TIME.getConstraint())))
        .sign(HMAC512(SECRET.getConstraint().getBytes()));
    res.addHeader(HEADER_STRING.getConstraint(), TOKEN_PREFIX.getConstraint() + token);
  }
}
