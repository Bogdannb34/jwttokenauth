package com.practice.jwttokenauth.mapper.auth;

import com.practice.jwttokenauth.dto.auth.request.RegisterRequest;
import com.practice.jwttokenauth.mapper.Mapper;
import com.practice.jwttokenauth.models.user.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterRequestToUserMapper implements Mapper<RegisterRequest, User> {

    @Override
    public User map(final RegisterRequest source) {
        final User user = new User();
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setEmail(source.getEmail());
        user.setPassword(source.getPassword());
        return user;
    }
}
