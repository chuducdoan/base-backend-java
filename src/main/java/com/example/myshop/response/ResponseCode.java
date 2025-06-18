package com.example.myshop.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResponseCode implements IResponseCode{

    SUCCESS("00", "Success"),
    USER_INACTIVE("01", "Tài khoản đã bị vô hiệu hóa."),
    USER_NOT_FOUND("10", "User not found"),
    EMAIL_EXIST("11", "Email already exists"),
    USERNAME_EXIST("12", "Username already exists"),
    PASSWORD_NO_MATCHES("13", "Password no matching"),
    INVALID_REQUEST("21", "Tham số không hợp lệ"),
    SYSTEM_ERROR("99", "System error"),
    FILE_EMPTY("30", "File không tồn tại. Vui lòng upload file hợp lệ");

    private String code;
    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
