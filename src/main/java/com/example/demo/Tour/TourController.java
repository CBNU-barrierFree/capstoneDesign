package com.example.demo.Tour;

import com.example.demo.DTO.TourDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController // 이 클래스가 REST API 컨트롤러임을 명시 (JSON 반환)
@RequestMapping("/api") // 이 컨트롤러의 모든 요청 URL은 "/api"로 시작
@RequiredArgsConstructor // final 필드인 tourRepository를 자동 생성자 주입
public class TourController {

    private final TourRepository tourRepository; // 관광지 데이터를 DB에서 조회하기 위한 JPA 리포지토리

    @GetMapping("/tour") // GET 방식으로 "/api/tour" 요청을 처리
    public Map<String, Object> getAllTours() {
        List<TourEntity> entities = tourRepository.findAll(); // 모든 관광지 엔티티를 DB에서 조회

        // 엔티티 리스트를 DTO 리스트로 변환
        List<TourDTO> dtoList = entities.stream().map(entity -> {
            TourDTO dto = new TourDTO(); // 빈 DTO 객체 생성
            dto.setTitle(entity.getTitle()); // 제목 설정
            dto.setAddress(entity.getAddress()); // 주소 설정
            dto.setCategory1(entity.getCategory1()); // 카테고리1 설정
            dto.setCategory2(entity.getCategory2()); // 카테고리2 설정
            dto.setCategory3(entity.getCategory3()); // 카테고리3 설정
            dto.setDescription(entity.getDescription()); // 설명 설정
            dto.setSubDescription(entity.getSubDescription()); // 부가 설명 설정
            dto.setIssuedDate(entity.getIssuedDate()); // 발행일 설정
            dto.setTel(entity.getTel()); // 전화번호 설정
            dto.setUrl(entity.getUrl()); // URL 설정
            dto.setCoordinates(entity.getCoordinates()); // 좌표 설정
            return dto; // 완성된 DTO 반환
        }).collect(Collectors.toList()); // 전체 리스트로 수집

        Map<String, Object> response = new HashMap<>(); // 최종 응답 Map
        Map<String, Object> body = new HashMap<>(); // "body" 키에 들어갈 내용
        Map<String, Object> wrapper = new HashMap<>(); // "response" 키에 들어갈 내용

        body.put("items", Collections.singletonMap("item", dtoList)); // items.item 형식으로 DTO 리스트 삽입
        wrapper.put("body", body); // body를 감싸는 wrapper
        response.put("response", wrapper); // 최종 응답 구조

        return response; // JSON 형태의 데이터 반환
    }
}