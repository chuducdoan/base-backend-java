package com.example.myshop.dto.response.auth;

import com.example.myshop.dto.response.user.UserLoginResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String access_token;
    private String refresh_token;
    private UserLoginResponse user;

    public static LoginResponseDTO from(String accessToken, String refreshToken, UserLoginResponse user) {
        return new LoginResponseDTO(accessToken, refreshToken, user);
    }
}
