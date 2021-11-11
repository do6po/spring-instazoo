package com.example.demo.payload.requests;

import com.example.demo.annotations.PasswordMatches;
import com.example.demo.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {
    @Email(message = "It should be email format")
    @NotEmpty(message = "Email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "firstname cannot be empty")
    private String firstname;

    @NotEmpty(message = "lastname cannot be empty")
    private String lastname;

    @NotEmpty(message = "username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6)
    private String password;
    private String confirmPassword;
}
