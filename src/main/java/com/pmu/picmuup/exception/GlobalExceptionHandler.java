// swagger 테스트를 위한 GlobalExceptionHandler 주석 처리

//package com.pmu.picmuup.exception;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(InvalidRecommendRequestException.class)
//    public ResponseEntity<Map<String, String>> handleInvalidRecommendRequest(InvalidRecommendRequestException ex) {
//
//        return ResponseEntity.badRequest().body(Map.of(
//                "error", "잘못된 추천 요청", "message", ex.getMessage()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
//        return ResponseEntity.badRequest().body(Map.of(
//                "error", "서버 오류", "message", ex.getMessage()
//        ));
//    }
//}
