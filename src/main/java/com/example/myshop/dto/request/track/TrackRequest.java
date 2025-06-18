package com.example.myshop.dto.request.track;

import com.example.myshop.validation.ValidationEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String trackUrl;

    @NotBlank
    private String imgUrl;

    @NotBlank
    private String category;
}
