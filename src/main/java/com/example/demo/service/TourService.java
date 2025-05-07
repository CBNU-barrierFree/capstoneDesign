package com.example.demo.service;

import com.example.demo.DTO.TourApiResponseDTO;
import com.example.demo.DTO.TourDTO;
import com.example.demo.entity.TourEntity;
import com.example.demo.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {
    private final TourRepository tourRepository;
    private final RestTemplate restTemplate;

    private String apiUrl = "http://api.kcisa.kr/openapi/API_TOU_049/request"
            + "?serviceKey=c0957f50-2a47-448e-b35a-7c0aa5415680"
            + "&numOfRows=1000";

    public TourService(TourRepository tourRepository, RestTemplate restTemplate) {
        this.tourRepository = tourRepository;
        this.restTemplate = restTemplate;
    }

    public void fetchAndSaveAllTours() {
        int page = 1;
        int totalSaved = 0;

        while (true) {
            String url = apiUrl + "&pageNo=" + page;

            // 요청 URL 출력
            System.out.println("요청 URL: " + url);

            TourApiResponseDTO response = null;

            try {
                response = restTemplate.getForObject(url, TourApiResponseDTO.class);
                System.out.println("응답 성공 여부: " + (response != null));
            } catch (Exception e) {
                System.out.println("API 호출 중 예외 발생:");
                e.printStackTrace();
                break;
            }

            // 응답 검증
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

            List<TourDTO> tourDTOs = response.getBody().getItems().getItem();

            // 데이터 수 출력
            System.out.println("page: " + page);
            System.out.println("현재 내려온 데이터 수: " + tourDTOs.size());

            for (TourDTO dto : tourDTOs) {
                Optional<TourEntity> existing = tourRepository.findByTitleAndAddress(dto.getTitle(), dto.getAddress());
                if (existing.isEmpty()) {
                    TourEntity entity = convertToEntity(dto);
                    tourRepository.save(entity);
                    totalSaved++;
                }
            }

            if (tourDTOs.size() < 1000) break;
            page++;
        }

        System.out.println("총 저장된 관광지 수: " + totalSaved);
    }

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
