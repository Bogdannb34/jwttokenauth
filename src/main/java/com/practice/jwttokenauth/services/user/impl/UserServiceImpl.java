package com.practice.jwttokenauth.services.user.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.jwttokenauth.exceptions.EmailAlreadyRegisterException;
import com.practice.jwttokenauth.exceptions.RefreshTokenIsMissingException;
import com.practice.jwttokenauth.exceptions.RoleNameNotFoundException;
import com.practice.jwttokenauth.exceptions.UserNotFoundException;
import com.practice.jwttokenauth.models.user.Role;
import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.repository.RoleRepository;
import com.practice.jwttokenauth.repository.UserRepository;
import com.practice.jwttokenauth.security.CustomUserDetails;
import com.practice.jwttokenauth.security.jwt.JwtTokenProvider;
import com.practice.jwttokenauth.services.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.practice.jwttokenauth.constants.Security.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        log.info("Fetching user with email : {} from database!", email);
        Optional<User> optionalUser = getUserRepository().findUserByEmail(email);
        return CustomUserDetails
                .build(optionalUser
                        .orElseThrow(() -> new UsernameNotFoundException(
                                String.format("User with email : %s not found!", email))));
    }

    @Override
    public void register(final User user) {
        Optional<User> optionalUser = getUserRepository().findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyRegisterException(String.format("Email '%s' already in use!", user.getEmail()));
        }
        user.setPassword(getPasswordEncoder().encode(user.getPassword()));
        final Role role = getRoleRepository().findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RoleNameNotFoundException("Role not found!"));
        user.setRoles(Collections.singleton(role));

        getUserRepository().save(user);
        log.info("User {} register successfully!", user.getFirstName() + " " + user.getLastName());
    }

    @Override
    public User findByEmail(final String email) {
        log.info("Fetching user {}", email);
        return getUserRepository().findUserByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("User with email: %s not found!", email)));
    }

    @Override
    public Boolean checkEmailInDB(final String email) {
        return getUserRepository().existsByEmail(email);
    }

    @Override
    public void refreshToken(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            try {
                final String refreshToken = authHeader.substring(TOKEN_PREFIX.length());
                final DecodedJWT decodedJWT = getTokenProvider().getJWTVerifier().verify(refreshToken);
                final String email = decodedJWT.getSubject();
                final User user = findByEmail(email);
                final String accessToken = getTokenProvider().generateJwtToken(CustomUserDetails.build(user));
                final Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RefreshTokenIsMissingException("Refresh token is missing");
        }
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Fetching all users");
        return getUserRepository().findAll();
    }

    @Override
    public void deleteUser(final Long id) {
        log.info("Deleting user with id: {}", id);
        getUserRepository().deleteById(id);
    }
}
