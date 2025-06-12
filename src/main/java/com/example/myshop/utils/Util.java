package com.example.myshop.utils;

import com.example.myshop.dto.response.smstest.SMSTestResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class Util {

    private static final Gson gson = new Gson();
    private static final Pattern VALID_PHONE_NUMBER = Pattern.compile("^(\\+?84[1-9]|0[1-9])\\d{8}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmpty(String data) {
        if(data == null || data.trim().length() == 0) return true;
        return false;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if(isEmpty(phoneNumber)) return false;
        Matcher matcher = VALID_PHONE_NUMBER.matcher(phoneNumber);
        return matcher.find();
    }

    public static SMSTestResponseDTO convertMessageFromByteToDTO (byte[] message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = gson.toJson(new String(message));
            // Kiểm tra và loại bỏ dấu ngoặc kép bao quanh
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1);
            }
            json= json.replace("\\", "");
            // Optional: Kiểm tra tính hợp lệ của JSON
            if (!isValidJson(json)) {
                throw new IllegalArgumentException("Invalid JSON: " + json);
            }
            SMSTestResponseDTO dto = objectMapper.readValue(json, SMSTestResponseDTO.class);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting message to DTO", e);
        }

    }

    // Phương thức kiểm tra JSON
    private static boolean isValidJson(String json) {
        try {
            new ObjectMapper().readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
