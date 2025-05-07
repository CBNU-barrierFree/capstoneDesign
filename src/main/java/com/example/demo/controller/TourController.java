package com.example.demo.controller;

import com.example.demo.DTO.TourDTO;
import com.example.demo.entity.TourEntity;
import com.example.demo.repository.TourRepository;
import com.example.demo.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TourController {

    private final TourRepository tourRepository;

    @GetMapping("/tour")
    public Map<String, Object> getAllTours() {
        List<TourEntity> entities = tourRepository.findAll();

        // TourEntity -> TourDTO 변환
        List<TourDTO> dtoList = entities.stream().map(entity -> {
            TourDTO dto = new TourDTO();
            dto.setTitle(entity.getTitle());
            dto.setAddress(entity.getAddress());
            dto.setCategory1(entity.getCategory1());
            dto.setCategory2(entity.getCategory2());
            dto.setCategory3(entity.getCategory3());
            dto.setDescription(entity.getDescription());
            dto.setSubDescription(entity.getSubDescription());
            dto.setIssuedDate(entity.getIssuedDate());
            dto.setTel(entity.getTel());
            dto.setUrl(entity.getUrl());
            dto.setCoordinates(entity.getCoordinates());
            return dto;
        }).collect(Collectors.toList());

        // 프론트에서 기대하는 JSON 구조로 포장
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> wrapper = new HashMap<>();

        body.put("items", Collections.singletonMap("item", dtoList));
        wrapper.put("body", body);
        response.put("response", wrapper);

        return response;
    }
}