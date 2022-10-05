package com.practice.jwttokenauth.controller.auth;

import com.practice.jwttokenauth.dto.auth.request.LoginRequest;
import com.practice.jwttokenauth.dto.auth.request.RegisterRequest;
import com.practice.jwttokenauth.dto.auth.response.MessageResponse;
import com.practice.jwttokenauth.mapper.Mapper;
import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.services.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Getter
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final Mapper<RegisterRequest, User> registerRequestUserMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody final RegisterRequest registerRequest) {
        if (getUserService().checkEmailInDB(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        getUserService().register(getRegisterRequestUserMapper().map(registerRequest));
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authentication(@Valid @RequestBody final LoginRequest loginRequest) {
        getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return ResponseEntity.ok(new MessageResponse("User logged in successfully!"));
    }
}
