package com.example.demo.hotspot;

import com.example.demo.hotspot.dto.HotspotListDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/accident-hotspots", produces = MediaType.APPLICATION_JSON_VALUE)
public class HotspotController {
    private final HotspotService service;
    public HotspotController(HotspotService service) { this.service = service; }

    @GetMapping("/ping")
    public Map<String, String> ping() { return Map.of("status", "ok"); }

    @GetMapping
    public ResponseEntity<?> getHotspots(
            @RequestParam("bbox") String bbox,
            @RequestParam(value = "year", required = false) Integer year,   // ✅ 선택 사항
            @RequestParam(value = "order", defaultValue = "accidents") String order,
            @RequestParam(value = "limit", defaultValue = "200") Integer limit
    ) {
        try {
            String[] p = bbox.split(",");
            if (p.length != 4) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "bad_request",
                        "message", "bbox 형식은 'west,south,east,north' 입니다."
                ));
            }
            double west  = Double.parseDouble(p[0].trim());
            double south = Double.parseDouble(p[1].trim());
            double east  = Double.parseDouble(p[2].trim());
            double north = Double.parseDouble(p[3].trim());
            if (west > east || south > north) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "bad_request",
                        "message", "bbox 값이 올바르지 않습니다. (west<=east, south<=north)"
                ));
            }

            HotspotListDto dto = service.searchInBBox(west, south, east, north, year, order, limit);
            return ResponseEntity.ok(dto);

        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "bad_request",
                    "message", "bbox 숫자 파싱 오류: " + nfe.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "server_error",
                    "message", e.getMessage()
            ));
        }
    }
}