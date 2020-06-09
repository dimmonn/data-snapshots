package com.luxosft.shapshot.auth.controller;

import com.luxosft.shapshot.auth.model.AuthUser;
import com.luxosft.shapshot.auth.repository.AuthUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final AuthUserRepository authUserRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserController(AuthUserRepository authUserRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.authUserRepository = authUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/sign-up")
  public void signUp(@RequestBody AuthUser user) {
    String encoded = bCryptPasswordEncoder.encode(user.getPassword());
    user.setPassword(encoded);
    authUserRepository.save(user);
  }
}
