package com.example.myshop.dto.request.user;

import com.example.myshop.validation.ValidationEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @NotNull
    private  Integer id;
    @NotBlank
    private String fullName;
    @ValidationEmail
    private String email;
}
