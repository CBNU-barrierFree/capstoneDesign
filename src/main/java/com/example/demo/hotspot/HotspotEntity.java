package com.example.demo.hotspot;

import jakarta.persistence.*;

@Entity
@Table(name = "hotspots",
        indexes = {
                @Index(name="idx_hotspot_bbox", columnList = "lng,lat"),
                @Index(name="idx_hotspot_year", columnList = "statYear")
        })
public class HotspotEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String name;

    @Column(nullable=false)
    private double lat;

    @Column(nullable=false)
    private double lng;

    @Column(nullable=false)
    private int accidents;

    @Column(nullable=false)
    private int casualties;

    /** 공공데이터 검색 연도 */
    @Column(nullable=false)
    private int statYear;

    // ===== Getter/Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public int getAccidents() { return accidents; }
    public void setAccidents(int accidents) { this.accidents = accidents; }

    public int getCasualties() { return casualties; }
    public void setCasualties(int casualties) { this.casualties = casualties; }

    public int getStatYear() { return statYear; }
    public void setStatYear(int statYear) { this.statYear = statYear; }
}
