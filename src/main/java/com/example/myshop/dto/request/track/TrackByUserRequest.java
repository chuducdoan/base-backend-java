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
public class TrackByUserRequest {
    @NotBlank
    private Integer id;

    private Integer current;

    private Integer pageSize;
}
