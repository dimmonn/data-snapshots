package com.luxosft.shapshot.auth.repository;

import com.luxosft.shapshot.auth.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
  AuthUser findByUsername(String username);
}