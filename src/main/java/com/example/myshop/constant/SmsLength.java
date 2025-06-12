package com.example.myshop.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsLength {

    MESSAGE_ID(255),
    KEYWORD(255),
    SENDER(255),
    DESTINATION(20),
    SHORT_MESSAGE(480)
    ;

    private final int maxLength;
}
