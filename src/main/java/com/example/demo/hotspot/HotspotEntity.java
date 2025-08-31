package com.example.demo.hotspot;

import jakarta.persistence.*;

@Entity
@Table(name = "hotspots")
public class HotspotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double lat;
    private Double lng;
    private Integer accidents;
    private Integer casualties;

    // DB 컬럼명은 year, 자바 필드는 statYear 로 사용
    @Column(name = "year", nullable = true)
    private Integer statYear;

    // --- Getter / Setter ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }
    public Integer getAccidents() { return accidents; }
    public Integer getCasualties() { return casualties; }
    public Integer getStatYear() { return statYear; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
    public void setAccidents(Integer accidents) { this.accidents = accidents; }
    public void setCasualties(Integer casualties) { this.casualties = casualties; }
    public void setStatYear(Integer statYear) { this.statYear = statYear; }
}
