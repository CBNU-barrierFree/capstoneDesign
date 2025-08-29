package com.example.demo.hotspot.dto;

import java.util.List;

public class HotspotListDto {
    public long total;
    public List<HotspotItemDto> items;
    public HotspotListDto() {}
    public HotspotListDto(long total, List<HotspotItemDto> items) {
        this.total = total; this.items = items;
    }
}
