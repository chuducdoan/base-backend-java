package com.example.myshop.service;

import com.example.myshop.dto.request.auth.LoginRequestDTO;
import com.example.myshop.dto.response.auth.LoginResponseDTO;
import com.example.myshop.dto.response.user.UserLoginResponse;
import com.example.myshop.entity.UserEntity;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import com.example.myshop.utils.Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private InvalidTokenService invalidTokenService;

    public BaseResponse login(LoginRequestDTO loginRequestDTO) {
        log.info("(login) request: {}", loginRequestDTO);
        UserEntity userEntity = userService.getByUsername(loginRequestDTO.getUsername());

        if (userEntity == null) {
            return BaseResponse.of(ResponseCode.USER_NOT_FOUND);
        }

        if (userEntity.getIsActive() == 0) {
            return BaseResponse.of(ResponseCode.USER_INACTIVE);
        }

        if (!Util.isEmpty(loginRequestDTO.getType()) && !loginRequestDTO.getType().equals(userEntity.getType())) {
            return  BaseResponse.of(ResponseCode.INVALID_REQUEST);
        }

        if (userEntity.getType().equals("SYSTEM")) {
            if(Util.isEmpty(loginRequestDTO.getPassword())) {
                return  BaseResponse.of(ResponseCode.INVALID_REQUEST);
            }
            if (!userService.equalPassword(loginRequestDTO.getPassword(), userEntity.getPassword())) {
                return BaseResponse.of(ResponseCode.PASSWORD_NO_MATCHES);
            }
        }




        // Tạo một HashMap để chứa các claims cần thiết cho JWT, chẳng hạn như tên người dùng.
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", loginRequestDTO.getUsername());
        String accessToken = jwtService.generateAccessToken(userEntity.getId(), claims);
        String refreshToken = jwtService.generateRefreshToken(userEntity.getId(), userEntity.getUsername());
        UserLoginResponse userTemp = new UserLoginResponse(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getIs_verify(), userEntity.getType(), userEntity.getRole());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(accessToken, refreshToken, userTemp);
        return BaseResponse.of(ResponseCode.SUCCESS, loginResponseDTO);
    }

    public BaseResponse logout(HttpServletRequest request) {
        log.info("(logout)");
        try {
            SecurityContextHolder.clearContext();
            // Vô hiệu hóa session nếu có
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            // Vô hiệu hóa token
            String accessToken = request.getHeader("Authorization");
            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                String jwtToken = accessToken.substring(7);
                invalidTokenService.addInvalidToken(jwtToken); // Vô hiệu hóa token
            }

            return BaseResponse.of(ResponseCode.SUCCESS);
        } catch (Exception e) {
            log.error("(logout) fail exception: {}", e.getMessage());
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }
}
