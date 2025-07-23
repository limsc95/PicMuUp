package com.pmu.picmuup.dto;

public record RecommendResponseDto(
        String title,
        String artist,
        String youtubeUrl,
        double similarity
) {}