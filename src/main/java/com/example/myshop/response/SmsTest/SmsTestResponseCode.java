package com.example.myshop.response.SmsTest;

import com.example.myshop.response.IResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsTestResponseCode implements IResponseCode {
    INVALID_PHONE_NUMBER("11", "Invalid phone number."),
    EXIST_MESSAGE_ID("12", "Message Id is exist."),
    MESSAGE_ID_OVER_MAX_LENGTH("13", "Message id exceeds max length"),
    KEYWORD_OVER_MAX_LENGTH("14", "Keyword id exceeds max length"),
    SENDER_OVER_MAX_LENGTH("15", "Sender exceeds max length"),
    DESTINATION_OVER_MAX_LENGTH("16", "Destination exceeds max length"),
    SHORT_MESSAGE_OVER_MAX_LENGTH("17", "Short message exceeds max length");

    private final String code;
    private final String message;
}
