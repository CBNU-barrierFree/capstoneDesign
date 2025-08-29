package com.example.demo.hotspot;

import com.example.demo.hotspot.dto.HotspotListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HotspotController {
    private final HotspotService service;
    public HotspotController(HotspotService service) { this.service = service; }

    // 예) /api/accident-hotspots?bbox=126.9,37.4,127.2,37.7&year=2021&order=accidents&limit=300
    @GetMapping("/api/accident-hotspots")
    public ResponseEntity<HotspotListDto> getHotspots(
            @RequestParam("bbox") String bbox,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "order", defaultValue = "accidents") String order,
            @RequestParam(value = "limit", defaultValue = "300") Integer limit
    ) {
        String[] p = bbox.split(",");
        if (p.length != 4) return ResponseEntity.badRequest().build();
        double west = Double.parseDouble(p[0]);
        double south = Double.parseDouble(p[1]);
        double east = Double.parseDouble(p[2]);
        double north = Double.parseDouble(p[3]);

        return ResponseEntity.ok(service.searchInBBox(west, south, east, north, year, order, limit));
    }
}
