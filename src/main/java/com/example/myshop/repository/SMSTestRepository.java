package com.example.myshop.repository;

import com.example.myshop.entity.SMSTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SMSTestRepository extends JpaRepository<SMSTestEntity, Long> {
    SMSTestEntity findByMessageId(String messageId);
}
