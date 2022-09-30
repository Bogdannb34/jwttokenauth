package com.practice.jwttokenauth.repository;

import com.practice.jwttokenauth.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
