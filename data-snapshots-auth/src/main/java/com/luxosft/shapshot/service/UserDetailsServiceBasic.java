package com.luxosft.shapshot.service;

import static java.util.Collections.emptyList;

import com.luxosft.shapshot.model.AuthUser;
import com.luxosft.shapshot.repository.AuthUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceBasic implements UserDetailsService {

  private final AuthUserRepository authUserRepository;

  public UserDetailsServiceBasic(AuthUserRepository authUserRepository) {
    this.authUserRepository = authUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AuthUser applicationUser = authUserRepository.findByUsername(username);
    if (applicationUser == null) {
      throw new UsernameNotFoundException(username);
    }
    return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
  }
}