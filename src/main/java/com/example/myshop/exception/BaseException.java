package com.example.myshop.exception;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseException extends RuntimeException {
    private String message;
    private String code;
    private int status;
    private Map<String, String> params;

    public BaseException() {
        this.status = 0;
        this.code = "";
        this.message = "";
        this.params = new HashMap<>();
    }
}
