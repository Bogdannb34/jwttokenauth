package com.practice.jwttokenauth.repository;

import com.practice.jwttokenauth.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(final String email);
}
