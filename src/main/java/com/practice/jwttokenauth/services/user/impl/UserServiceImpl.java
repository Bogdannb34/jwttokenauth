package com.practice.jwttokenauth.services.user.impl;

import com.practice.jwttokenauth.exceptions.EmailAlreadyRegisterException;
import com.practice.jwttokenauth.exceptions.RoleNameNotFoundException;
import com.practice.jwttokenauth.models.user.Role;
import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.repository.RoleRepository;
import com.practice.jwttokenauth.repository.UserRepository;
import com.practice.jwttokenauth.security.CustomUserDetails;
import com.practice.jwttokenauth.services.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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
        final Role role = getRoleRepository().findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new RoleNameNotFoundException("Role not found!"));
        user.setRoles(Collections.singleton(role));

        getUserRepository().save(user);
        log.info("User {} register successfully!", user.getFirstName() + " " + user.getLastName());
    }

    @Override
    public Boolean checkEmailInDB(final String email) {
        return getUserRepository().existsByEmail(email);
    }
}
