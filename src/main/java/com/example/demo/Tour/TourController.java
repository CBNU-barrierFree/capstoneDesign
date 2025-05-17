package com.example.demo.Tour;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TourController {

    private final TourRepository tourRepository;

    public TourController(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @GetMapping(value = "/tour", produces = "application/json")
    public Map<String, Object> getAllTours() {
        List<TourEntity> tourList = tourRepository.findAll();

        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("item", tourList); // 관광지 리스트

        Map<String, Object> itemsMap = new HashMap<>();
        itemsMap.put("items", itemMap); // items → item

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("body", itemsMap); // body → items

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("response", bodyMap); // response → body

        return responseMap;
    }
}