
package com.luxosft.shapshot.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityConstraints {
  SECRET("SecretKeyToGenJWTs"),

  EXPIRATION_TIME("464000000"),

  TOKEN_PREFIX("Bearer "),

  HEADER_STRING("Authorization"),

  SIGN_UP_URL("/v1/users/sign-up");

  private String constraint;


}
