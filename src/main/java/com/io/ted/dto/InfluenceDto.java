package com.io.ted.dto;

public record InfluenceDto(
        String speaker,
        double score,
        long totalViews,
        long totalLikes,
        int talksCount
) {}
