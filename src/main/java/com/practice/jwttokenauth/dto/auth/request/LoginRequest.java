package com.practice.jwttokenauth.dto.auth.request;

import com.practice.jwttokenauth.dto.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest extends BaseDTO<Long> {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

}
