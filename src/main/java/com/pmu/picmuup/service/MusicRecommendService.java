package com.pmu.picmuup.service;

import com.pmu.picmuup.dto.RecommendResponseDto;
import com.pmu.picmuup.dto.ScoredMusic;
import com.pmu.picmuup.entity.Music;
import com.pmu.picmuup.exception.InvalidRecommendRequestException;
import com.pmu.picmuup.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MusicRecommendService {

    private final MusicRepository musicRepository;

    private final WebClient webClient;

    final Integer topK = 2;

    // 이미지 벡터 추출
    public List<Float> getImageEmbedding(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();

        ByteArrayResource resource = new ByteArrayResource(bytes){
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, ByteArrayResource> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        return Objects.requireNonNull(webClient.post()
                        .uri("http://localhost:8000/embed/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, List<Float>>>() {
                        })
                        .block())
                .get("embedding");
    }

    // 텍스트 벡터 추출
    public List<Float> getTextEmbedding(String text){
        System.out.println("전송하는 텍스트: " + text);
        return Objects.requireNonNull(webClient.post()
                        .uri("http://localhost:8000/embed/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("text", text))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, List<Float>>>() {
                        })
                        .block())
                .get("embedding");
    }

    // 이미지를 통한 추천 실행
    public List<RecommendResponseDto> recommendByImage(MultipartFile imageFile) throws IOException {
        List<Float> imageEmbedding = getImageEmbedding(imageFile);

        return recommendByEmbedding(imageEmbedding);
    }

    // 텍스트를 통한 추천 실행
    public List<RecommendResponseDto> recommendByText(String text){
        List<Float> textEmbedding = getTextEmbedding(text);

        return recommendByEmbedding(textEmbedding);
    }

    // 이미지와 텍스트를 통한 추천 실행
    public List<RecommendResponseDto> recommendByImageAndText(MultipartFile imageFile, String text) throws IOException {

        if ((imageFile == null || imageFile.isEmpty()) && (text == null || text.isBlank())) {
            throw new InvalidRecommendRequestException("파일 또는 텍스트 중 하나는 필요합니다.");
        }

        if (imageFile == null || imageFile.isEmpty()) {
            return recommendByText(text);
        }
        if (text == null || text.isBlank()) {
            return recommendByImage(imageFile);
        }

        List<Float> imageEmbedding = getImageEmbedding(imageFile);
        List<Float> textEmbedding = getTextEmbedding(text);

        // 둘의 평군 벡터 계산
        List<Float> imageAndTextEmbedding = new ArrayList<>();

        for (int i = 0; i < imageEmbedding.size(); i++) {
            imageAndTextEmbedding.add((imageEmbedding.get(i) + textEmbedding.get(i)) / 2);
        }

        return recommendByEmbedding(imageAndTextEmbedding);
    }

    private List<RecommendResponseDto> recommendByEmbedding(List<Float> queryEmbedding) {
        List<Music> allMusic = musicRepository.findAll();

        return allMusic.stream()
                .map(music -> {
                    List<Double> musicVector = parseEmbedding(music.getEmbedding());
                    double score = cosineSimilarity(queryEmbedding, musicVector);
                    return new ScoredMusic(music, score);
                })
                .sorted(Comparator.comparingDouble(ScoredMusic::getScore).reversed())
                .limit(topK)
                .map(ScoredMusic::toDto)
                .toList();
    }


    private List<Double> parseEmbedding(String embeddingString) {
        return Stream.of(embeddingString.replace("[", "").replace("]", "").split(","))
                .map(String::trim)
                .map(Double::parseDouble)
                .toList();
    }

    private double cosineSimilarity(List<Float> a, List<Double> b) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
