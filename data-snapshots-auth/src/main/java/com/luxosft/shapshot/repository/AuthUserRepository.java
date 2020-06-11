package com.luxosft.shapshot.repository;

import com.luxosft.shapshot.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
  AuthUser findByUsername(String username);
}