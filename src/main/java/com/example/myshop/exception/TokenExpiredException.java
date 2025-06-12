package com.example.myshop.exception;

public class TokenExpiredException extends BaseException{
    public TokenExpiredException() {
        setCode("401");
        setStatus(401);
    }
}
