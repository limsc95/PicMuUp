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

    //@PostMapping("/image")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<RecommendResponseDto> recommendFromImage(
            @RequestParam("file") MultipartFile file) throws IOException {

        return musicRecommendService.recommendByImage(file);
    }

    @PostMapping("/text")
    public List<RecommendResponseDto> recommendFromText(
            @RequestParam("text") String text
    ) throws IOException {

        return musicRecommendService.recommendByText(text);
    }
}
