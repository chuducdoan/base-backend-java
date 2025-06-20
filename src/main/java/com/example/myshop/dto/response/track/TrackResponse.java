package com.example.myshop.dto.response.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackResponse {
    private Long id;

    private String title;

    private String description;

    private String trackUrl;

    private String imgUrl;

    private String category;

    private Long countLike;

    private Long countPlay;

}
