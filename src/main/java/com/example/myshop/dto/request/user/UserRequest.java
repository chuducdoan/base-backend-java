package com.example.myshop.dto.request.user;

import com.example.myshop.validation.ValidationEmail;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String fullName;

    @NotBlank
    @Min(value = 6)
    private String password;

    @ValidationEmail
    private String email;
}
