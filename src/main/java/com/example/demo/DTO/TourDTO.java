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

    public TourDTO() {} // ✅ 반드시 필요

    // ✅ 모든 setter가 있어야 함
    public void setTitle(String title) { this.title = title; }
    public void setIssuedDate(String issuedDate) { this.issuedDate = issuedDate; }
    public void setCategory1(String category1) { this.category1 = category1; }
    public void setCategory2(String category2) { this.category2 = category2; }
    public void setCategory3(String category3) { this.category3 = category3; }
    public void setDescription(String description) { this.description = description; }
    public void setSubDescription(String subDescription) { this.subDescription = subDescription; }
    public void setTel(String tel) { this.tel = tel; }
    public void setUrl(String url) { this.url = url; }
    public void setAddress(String address) { this.address = address; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    // getter 생략 가능하지만 있으면 더 좋음
}

