package com.io.ted.dto;

public record TedTalkDto(
        String title,
        String speaker,
        Integer year,
        Long views,
        Long likes,
        String url
) {}
