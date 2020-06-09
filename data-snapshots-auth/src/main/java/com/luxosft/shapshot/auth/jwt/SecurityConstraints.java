
package com.luxosft.shapshot.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityConstraints {
  SECRET("SecretKeyToGenJWTs"),

  EXPIRATION_TIME("464000000"),

  TOKEN_PREFIX("Bearer "),

  HEADER_STRING("Authorization"),

  SIGN_UP_URL("/users/sign-up");

  private String constraint;


}
