package com.practice.jwttokenauth.controller.auth;

import com.practice.jwttokenauth.dto.auth.request.LoginRequest;
import com.practice.jwttokenauth.dto.auth.request.RegisterRequest;
import com.practice.jwttokenauth.dto.auth.response.JwtResponse;
import com.practice.jwttokenauth.dto.auth.response.MessageResponse;
import com.practice.jwttokenauth.mapper.Mapper;
import com.practice.jwttokenauth.models.user.User;
import com.practice.jwttokenauth.security.CustomUserDetails;
import com.practice.jwttokenauth.security.jwt.JwtTokenProvider;
import com.practice.jwttokenauth.services.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Getter
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
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
        final User user = getUserService().findByEmail(loginRequest.getEmail());
        final CustomUserDetails userDetails = CustomUserDetails.build(user);
        final String jwt = getTokenProvider().generateJwtToken(userDetails);
        final Collection<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(userDetails.getUser().getId(), userDetails.getUsername(), jwt, roles));
    }
}
