
package com.luxosft.shapshot.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SecurityConstraints {

  @Value("${secret}")
  private String secret;
  @Value("${expiration_time}")
  private String expirationTime;
  @Value("${token_prefix}")
  private String tokenPrefix;
  @Value("${auth_header}")
  private String headerString;
  @Value("${sign_up_url}")
  private String signUpUrl;
}