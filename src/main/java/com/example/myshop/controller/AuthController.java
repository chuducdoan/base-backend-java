package com.example.myshop.controller;

import com.example.myshop.dto.request.auth.LoginRequestDTO;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import com.example.myshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public BaseResponse login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @PostMapping(value = "/logout")
    public BaseResponse logout(@RequestHeader("Authorization") String authorizationHeader, HttpServletRequest request) {
        return authService.logout(request);
    }
}
