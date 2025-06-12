package com.example.myshop.dto.request.smstest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMSSendRequestDTO {
    private String messageId;

    private String destination;

    private String sender;

    private String keyword;

    private String shortMessage;

    private String partnerCode;

    private String encryptMessage;

    private Integer isEncrypt;

    private Integer type;

    private Long requestTime;

    private String sercretKey;
}
