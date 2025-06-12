package com.example.myshop.dto.request.auth;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Min(value = 6)
    private String password;
}
