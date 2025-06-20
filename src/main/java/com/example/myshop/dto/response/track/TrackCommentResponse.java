package com.example.myshop.dto.response.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackCommentResponse {
    private Long id;

    private String content;

    private Long moment;

    private Integer userId;

    private String userName;

    private LocalDateTime createdAt;
}
