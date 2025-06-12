package com.example.myshop.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String fullName;

    public static LoginResponseDTO from(String accessToken, String refreshToken, String fullName) {
        return new LoginResponseDTO(accessToken, refreshToken, fullName);
    }
}
