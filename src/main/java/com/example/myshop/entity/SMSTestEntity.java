package com.example.myshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sms_test")
@Getter
public class SMSTestEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MESSAGE_ID", unique = true)
    private String messageId;

    @Column(name = "DESTINATION")
    private String destination;

    @Column(name = "SENDER")
    private String sender;

    @Column(name = "KEYWORD")
    private String keyword;

    @Column(name = "SHORT_MESSAGE")
    private String shortMessage;

    @Column(name = "ENCRYPT_MESSAGE")
    private String encryptMessage;

    @Column(name = "IS_ENCRYPT")
    private Integer isEncrypt;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "REQUEST_TIME")
    private Long requestTime;

    @Column(name = "PARTNER_CODE")
    private String partnerCode;

    @Column(name = "SERCRET_KEY")
    private String sercretKey;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SENT_TIME")
    private Long sentTime;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSentTime() {
        return sentTime;
    }

    public void setSentTime(Long sentTime) {
        this.sentTime = sentTime;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public void setEncryptMessage(String encryptMessage) {
        this.encryptMessage = encryptMessage;
    }

    public void setIsEncrypt(Integer isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public void setSercretKey(String sercretKey) {
        this.sercretKey = sercretKey;
    }

    public Long getId() {
        return id;
    }
}
