package com.pmu.picmuup.controller;

import com.pmu.picmuup.dto.RecommendResponseDto;
import com.pmu.picmuup.service.MusicRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class MusicRecommendController {

    private final MusicRecommendService musicRecommendService;

    // 이미지를 통한 추천
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<RecommendResponseDto> recommendFromImage(
            @RequestParam("file") MultipartFile file) throws IOException {

        return musicRecommendService.recommendByImage(file);
    }

    // 텍스트를 통한 추천
    @PostMapping("/text")
    public List<RecommendResponseDto> recommendFromText(@RequestParam("text") String text){

        return musicRecommendService.recommendByText(text);
    }

    // 이미지와 텍스트 모두를 통한 추천
    @PostMapping(value = "/combined", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<RecommendResponseDto> recommendFromImageAndText(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "text", required = false) String text) throws IOException {

        return musicRecommendService.recommendByImageAndText(file, text);
    }
}
