package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {
    private List<TourDTO> item;

    // Getters and Setters
    public List<TourDTO> getItem() {
        return item;
    }
    public void setItem(List<TourDTO> item) {
        this.item = item;
    }
}
