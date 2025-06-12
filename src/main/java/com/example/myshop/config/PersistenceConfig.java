package com.example.myshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Kích hoạt tính năng JPA Auditing trong Spring Data JPA
// JPA Auditing cho phép bạn tự động theo dõi các thông tin như thời gian tạo và thời gian cập nhật cho các entity CSDL
@Configuration
@EnableJpaAuditing
public class PersistenceConfig {
}
