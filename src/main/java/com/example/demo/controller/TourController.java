package com.example.demo.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class TourController {

    @GetMapping("/tour")
    public ResponseEntity<String> getTourData() {
        // 1. 요청 URL
        String url = "http://api.kcisa.kr/openapi/API_TOU_049/request"
                + "?serviceKey=c0957f50-2a47-448e-b35a-7c0aa5415680"
                + "&numOfRows=1000"
                + "&pageNo=1";

        // 2. 요청 헤더 설정 (Accept: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");

        // 3. 요청 객체 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 4. RestTemplate으로 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,                     // 요청 URL
                HttpMethod.GET,          // GET 방식
                entity,                  // 요청 헤더 포함
                String.class             // 응답 형식
        );

        // 5. 결과를 프론트엔드로 반환
        return ResponseEntity.ok(response.getBody());
    }
}
