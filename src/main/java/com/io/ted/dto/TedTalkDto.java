package com.io.ted.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record TedTalkDto(

        @NotBlank(message = "Title must not be blank")
        String title,

        @NotBlank(message = "Speaker must not be blank")
        String speaker,

        @Min(value = 1900, message = "Year must be a valid year")
        Integer year,

        @PositiveOrZero(message = "Views must be >= 0")
        Long views,

        @PositiveOrZero(message = "Likes must be >= 0")
        Long likes,

        String url
) {}
