package com.example.myshop.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotNull
    private Integer id;

    @NotBlank
    private String password;

    @NotBlank
    @Min(value = 6)
    private String newPassword;

    @NotBlank
    @Min(value = 6)
    private String confirmPassword;
}
