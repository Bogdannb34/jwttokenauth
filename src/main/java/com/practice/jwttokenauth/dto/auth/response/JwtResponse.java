package com.practice.jwttokenauth.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class JwtResponse {

    private Long id;
    private String email;
    private String accessToken;
    private Collection<String> roles;
}
