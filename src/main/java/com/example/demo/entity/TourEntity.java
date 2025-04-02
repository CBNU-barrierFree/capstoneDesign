package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "tour_entity")
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // API 응답에서 받아올 필드 (예시)
    private String tourName;
    private String description;

    // Getter와 Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTourName() {
        return tourName;
    }
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    // 추가 Getter/Setter...
}