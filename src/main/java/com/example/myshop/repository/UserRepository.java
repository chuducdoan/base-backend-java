package com.example.myshop.repository;

import com.example.myshop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserEntity getByUsername(String username);
}
