package com.practice.jwttokenauth.controller.auth;

import com.practice.jwttokenauth.dto.auth.request.RegisterRequest;
import com.practice.jwttokenauth.dto.auth.response.MessageResponse;
import com.practice.jwttokenauth.mapper.Mapper;
import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.services.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final Mapper<RegisterRequest, User> registerRequestUserMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        getUserService().register(getRegisterRequestUserMapper().map(registerRequest));
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
