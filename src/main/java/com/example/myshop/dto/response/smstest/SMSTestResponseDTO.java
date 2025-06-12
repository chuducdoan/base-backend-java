package com.example.myshop.dto.response.smstest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMSTestResponseDTO {
    private Long id;

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

    private Integer status;

    private String description;

    private Long sentTime;
}
