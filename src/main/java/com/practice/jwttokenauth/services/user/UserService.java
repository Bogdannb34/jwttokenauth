package com.practice.jwttokenauth.services.user;

import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.services.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public interface UserService extends UserDetailsService, BaseService<Long, User> {

    void register(final User user);

    User findByEmail(final String email);

    Boolean checkEmailInDB(final String email);

    void refreshToken(final HttpServletRequest request, final HttpServletResponse response) throws IOException;

    Collection<User> getUsers();

    void deleteUser(final Long id);

}
