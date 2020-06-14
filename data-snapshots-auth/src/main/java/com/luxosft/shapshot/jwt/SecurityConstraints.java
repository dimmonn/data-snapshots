
package com.luxosft.shapshot.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
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