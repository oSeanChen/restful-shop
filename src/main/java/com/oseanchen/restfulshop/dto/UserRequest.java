package com.oseanchen.restfulshop.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "username can not be blank")

    private String username;

    @NotBlank(message = "password can not be blank")
    @Size(min = 6, message = "password length at least 6 characters")
    private String password;

    @NotBlank(message = "email can not be blank")
    @Email(message = "invalid email format")
    private String email;
}
