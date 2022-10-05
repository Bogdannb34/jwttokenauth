package com.practice.jwttokenauth.services.user;

import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.services.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, BaseService<Long, User> {

    void register(final User user);

}
