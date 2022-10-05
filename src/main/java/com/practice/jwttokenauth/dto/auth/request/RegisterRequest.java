package com.practice.jwttokenauth.dto.auth.request;

import com.practice.jwttokenauth.models.BaseModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest extends BaseModel<Long> {

    @NotEmpty(message = "First name must be longer than 3 characters!")
    @Size(min = 3, max = 20)
    private String firstName;

    @NotEmpty(message = "Last name must be longer than 3 characters!")
    @Size(min = 3, max = 20)
    private String lastName;

    @Size(max = 100)
    @NotEmpty(message = "E-mail field must not be empty!")
    @Email(message = "Email field should follow the pattern: example@example.com")
    private String email;

    @NotEmpty
    @Size(min = 6, max = 40)
    private String password;

    @NotEmpty
    private String confirmPassword;
}
