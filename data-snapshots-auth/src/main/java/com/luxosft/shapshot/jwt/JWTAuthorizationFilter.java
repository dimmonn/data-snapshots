
package com.luxosft.shapshot.jwt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
  private final SecurityConstraints securityConstraints;
  public JWTAuthorizationFilter(SecurityConstraints securityConstraints, AuthenticationManager authManager) {
    super(authManager);
    this.securityConstraints=securityConstraints;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader(securityConstraints.getHeaderString());

    if (header == null || !header.startsWith(securityConstraints.getTokenPrefix())) {
      chain.doFilter(req, res);
      return;
    }
    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(securityConstraints.getHeaderString());
    if (token != null) {
      String user = JWT.require(Algorithm.HMAC512(securityConstraints.getSecret().getBytes()))
          .build()
          .verify(token.replace(securityConstraints.getTokenPrefix(), ""))
          .getSubject();
      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }
}
