package com.example.myshop.dto.request.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackCommentRequest {
    @NotBlank
    private Integer current;

    private Integer pageSize;

    private Long trackId;
}
