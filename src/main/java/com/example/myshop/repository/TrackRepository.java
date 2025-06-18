package com.example.myshop.repository;

import com.example.myshop.entity.TrackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    Page<TrackEntity> findByCreatedBy(Integer createdBy, Pageable pageable);
}
