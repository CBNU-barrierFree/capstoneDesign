package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourDTO {

    private String title;
    private String issuedDate;
    private String category1;
    private String category2;
    private String category3;
    private String description;
    private String subDescription;
    private String tel;
    private String url;
    private String address;
    private String coordinates;

    public TourDTO() {} // 반드시 필요
}