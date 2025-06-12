package com.example.myshop.exception;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException() {
        setCode("400");
        setStatus(400);
    }
}
