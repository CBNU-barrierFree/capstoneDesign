package com.example.demo.hotspot.dto;

public class HotspotItemDto {
    public String id;
    public String name;
    public double lat;
    public double lng;
    public int accidents;
    public int casualties;
    public Integer year; // null 가능

    public HotspotItemDto(String id, String name, double lat, double lng,
                          int accidents, int casualties, Integer year) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.accidents = accidents;
        this.casualties = casualties;
        this.year = year;
    }
}
