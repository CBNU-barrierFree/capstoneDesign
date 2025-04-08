package com.example.demo.controller;

import com.example.demo.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/api/tour")
    public String fetchAndSaveTourData() {
        tourService.fetchAndSaveAllTours(); // 여기서 실제로 DB 저장 로직 실행
        return "데이터 저장 완료";
    }
}