package com.example.myshop.repository;

import com.example.myshop.dto.view.CommentView;
import com.example.myshop.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query(
            value = "SELECT c.id AS id, c.content AS content, c.moment AS moment, c.createdAt AS createdAt, " +
                    "u.id AS userId, u.username AS userName, u.type AS type " +
                    "FROM CommentEntity c JOIN c.user u WHERE c.track.id = :trackId",
            countQuery = "SELECT COUNT(c) FROM CommentEntity c WHERE c.track.id = :trackId"
    )
    Page<CommentView> findByTrackIdProjected(@Param("trackId") Long trackId, Pageable pageable);
}
