package com.example.demo.hotspot.dto;

public class HotspotItemDto {
    public Long id;
    public String name;
    public double lat;
    public double lng;
    public int accidents;
    public int casualties;
    public int year;

    public HotspotItemDto() {}
    public HotspotItemDto(Long id, String name, double lat, double lng, int accidents, int casualties, int year) {
        this.id = id; this.name = name; this.lat = lat; this.lng = lng;
        this.accidents = accidents; this.casualties = casualties; this.year = year;
    }
}
