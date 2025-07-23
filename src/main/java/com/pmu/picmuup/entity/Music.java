package com.pmu.picmuup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "music")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String genre;

    @Column(name = "emotion_tags")
    private String emotionTags;

    @Column(columnDefinition = "TEXT")
    private String embedding;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}