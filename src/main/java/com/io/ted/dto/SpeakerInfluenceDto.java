package com.io.ted.dto;

public record SpeakerInfluenceDto(
        String speaker,
        long totalViews,
        long totalLikes,
        double influence
) {}
