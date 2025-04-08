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
        int totalCount = Integer.MAX_VALUE; // 초기값은 매우 큰 값

        while ((pageNo - 1) * numOfRows < totalCount) {
            String url = "http://api.kcisa.kr/openapi/API_TOU_049/request"
                    + "?serviceKey=c0957f50-2a47-448e-b35a-7c0aa5415680"  // 실제 서비스 키로 대체
                    + "&numOfRows=" + numOfRows
                    + "&pageNo=" + pageNo;
            // 필요하다면 "&_type=json" 파라미터 추가 가능
            // + "&_type=json";

            // JSON 응답을 요청하기 위해 Accept 헤더를 지정하는 방법은,
            // RestTemplate에 인터셉터를 추가하거나 클라이언트별로 설정할 수 있으나,
            // 기본적으로 스프링의 MappingJackson2HttpMessageConverter가 적용되므로,
            // URL이나 환경에 따라 JSON 응답이 반환된다면 별도 설정 없이도 매핑됩니다.

            TourApiResponseDTO tourResponse = restTemplate.getForObject(url, TourApiResponseDTO.class);
            if (tourResponse == null || tourResponse.getBody() == null ||
                    tourResponse.getBody().getItems() == null ||
                    tourResponse.getBody().getItems().getItem() == null) {
                break; // 응답이 올바르지 않으면 종료
            }

            try {
                totalCount = Integer.parseInt(tourResponse.getBody().getTotalCount());
            } catch (NumberFormatException e) {
                break;
            }

            for (TourDTO dto : tourResponse.getBody().getItems().getItem()) {
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
                tourRepository.save(entity);
            }
            System.out.println("Page " + pageNo + " 저장 완료");
            pageNo++;
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void scheduledFetchAndSaveTours() {
        fetchAndSaveAllTours();
    }
}
