package com.example.myshop.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InvalidTokenService {
    // Sử dụng ConcurrentHashMap để đảm bảo an toàn khi truy cập từ nhiều luồng
    private final Set<String> invalidTokens = ConcurrentHashMap.newKeySet();

    // Phương thức để thêm token vào danh sách đã vô hiệu hóa
    public void addInvalidToken(String token) {
        invalidTokens.add(token);
    }

    // Phương thức kiểm tra xem token có nằm trong danh sách đã vô hiệu hóa không
    public boolean isTokenInvalid(String token) {
        return invalidTokens.contains(token);
    }
}
