package com.pmu.picmuup.dto;

import com.pmu.picmuup.entity.Music;
import lombok.*;

@Getter
@AllArgsConstructor
public class ScoredMusic {
    private Music music;
    private double score;

    public RecommendResponseDto toDto() {
        return new RecommendResponseDto(
                music.getTitle(),
                music.getArtist(),
                music.getYoutubeUrl(),
                score
        );
    }
}