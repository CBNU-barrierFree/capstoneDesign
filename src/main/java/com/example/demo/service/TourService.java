package com.example.demo.service;

import com.example.demo.DTO.TourApiResponseDTO;
import com.example.demo.DTO.TourDTO;
import com.example.demo.entity.TourEntity;
import com.example.demo.repository.TourRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TourService {
    private final TourRepository tourRepository;
    private final RestTemplate restTemplate;

    public TourService(TourRepository tourRepository, RestTemplate restTemplate) {
        this.tourRepository = tourRepository;
        this.restTemplate = restTemplate;
    }
    public void fetchAndSaveAllTours() {
        int pageNo = 1;
        int numOfRows = 10;
        int totalCount = Integer.MAX_VALUE; // 초기값은 매우 큰 값으로 설정
        while ((pageNo - 1) * numOfRows < totalCount) {
            String url = "http://api.kcisa.kr/openapi/API_TOU_049/request"
                    + "?serviceKey=c0957f50-2a47-448e-b35a-7c0aa5415680"
                    + "&numOfRows=" + numOfRows
                    + "&pageNo=" + pageNo;

            // API 호출
            TourApiResponseDTO apiResponse = restTemplate.getForObject(url, TourApiResponseDTO.class);
            if (apiResponse == null || apiResponse.getBody() == null
                    || apiResponse.getBody().getItems() == null
                    || apiResponse.getBody().getItems().getItem() == null) {
                break; // 응답이 없으면 종료
            }

            // 응답에서 전체 개수(totalCount)를 추출 (문자열이라면 숫자로 변환)
            try {
                totalCount = Integer.parseInt(apiResponse.getBody().getTotalCount());
            } catch (NumberFormatException e) {
                // totalCount 파싱 실패시 break 처리
                break;
            }

            // 현재 페이지의 데이터 처리
            for (TourDTO dto : apiResponse.getBody().getItems().getItem()) {
                TourEntity entity = new TourEntity();
                entity.setTitle(dto.getTitle());
                // 필요한 필드들을 매핑합니다.
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

                tourRepository.save(entity);
            }
            System.out.println("Page " + pageNo + " 저장 완료");
            pageNo++; // 다음 페이지 호출
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void scheduledFetchAndSaveTours() {
        fetchAndSaveAllTours();
    }
}
