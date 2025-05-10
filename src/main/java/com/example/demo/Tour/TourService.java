package com.example.demo.Tour;

import com.example.demo.DTO.TourApiResponseDTO;
import com.example.demo.DTO.TourDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service // 이 클래스가 서비스 계층의 컴포넌트임을 명시 (Spring이 자동으로 Bean 등록)
public class TourService {
    private final TourRepository tourRepository; // 관광지 정보를 저장하기 위한 JPA 레포지토리
    private final RestTemplate restTemplate; // 외부 API 호출을 위한 RestTemplate

    // OpenAPI 기본 URL + 공공 서비스 키 + 고정된 row 수
    private String apiUrl = "http://api.kcisa.kr/openapi/API_TOU_049/request"
            + "?serviceKey=c0957f50-2a47-448e-b35a-7c0aa5415680"
            + "&numOfRows=1000";

    // 생성자를 통한 의존성 주입
    public TourService(TourRepository tourRepository, RestTemplate restTemplate) {
        this.tourRepository = tourRepository;
        this.restTemplate = restTemplate;
    }

    // 모든 페이지의 관광지 데이터를 API에서 가져와 DB에 저장
    public void fetchAndSaveAllTours() {
        int page = 1; // 시작 페이지
        int totalSaved = 0; // 저장된 총 항목 수

        while (true) {
            String url = apiUrl + "&pageNo=" + page; // 페이지 번호를 포함한 API 요청 URL 생성

            System.out.println("요청 URL: " + url); // 요청 URL 출력

            TourApiResponseDTO response = null;

            try {
                // API 호출 및 응답 객체 매핑
                response = restTemplate.getForObject(url, TourApiResponseDTO.class);
                System.out.println("응답 성공 여부: " + (response != null));
            } catch (Exception e) {
                // 예외 발생 시 로그 출력 후 중단
                System.out.println("API 호출 중 예외 발생:");
                e.printStackTrace();
                break;
            }

            // 응답 데이터 검증
            if (response == null) {
                System.out.println("응답이 null입니다.");
                break;
            }
            if (response.getBody() == null) {
                System.out.println("Body가 null입니다.");
                break;
            }
            if (response.getBody().getItems() == null) {
                System.out.println("Items가 null입니다.");
                break;
            }

            List<TourDTO> tourDTOs = response.getBody().getItems().getItem(); // 실제 관광지 데이터 리스트 추출

            // 현재 페이지 정보 출력
            System.out.println("page: " + page);
            System.out.println("현재 내려온 데이터 수: " + tourDTOs.size());

            for (TourDTO dto : tourDTOs) {
                // title과 address 기준으로 중복 확인
                Optional<TourEntity> existing = tourRepository.findByTitleAndAddress(dto.getTitle(), dto.getAddress());
                if (existing.isEmpty()) {
                    // 중복이 아니라면 엔티티로 변환 후 저장
                    TourEntity entity = convertToEntity(dto);
                    tourRepository.save(entity);
                    totalSaved++; // 저장 개수 증가
                }
            }

            // 더 이상 데이터가 없으면 반복 종료
            if (tourDTOs.size() < 1000) break;
            page++; // 다음 페이지로 이동
        }

        System.out.println("총 저장된 관광지 수: " + totalSaved); // 총 저장 결과 출력
    }

    // DTO → Entity로 변환하는 유틸리티 메서드
    private TourEntity convertToEntity(TourDTO dto) {
        TourEntity entity = new TourEntity();
        entity.setTitle(dto.getTitle());
        entity.setIssuedDate(dto.getIssuedDate());
        entity.setCategory1(dto.getCategory1());
        entity.setCategory2(dto.getCategory2());
        entity.setCategory3(dto.getCategory3());
        entity.setDescription(dto.getDescription());
        entity.setSubDescription(dto.getSubDescription());
        entity.setTel(dto.getTel());
        entity.setUrl(dto.getUrl());
        entity.setAddress(dto.getAddress());
        entity.setCoordinates(dto.getCoordinates());
        return entity;
    }
}