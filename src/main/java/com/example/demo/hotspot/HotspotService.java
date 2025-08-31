package com.example.demo.hotspot;

import com.example.demo.hotspot.dto.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotspotService {
    private final HotspotRepository repo;
    public HotspotService(HotspotRepository repo) { this.repo = repo; }

    public HotspotListDto searchInBBox(
            double west, double south, double east, double north,
            Integer year, String order, int limit
    ) {
        Sort sort = "casualties".equalsIgnoreCase(order)
                ? Sort.by(Sort.Direction.DESC, "casualties")
                : Sort.by(Sort.Direction.DESC, "accidents");

        int size = Math.min(Math.max(limit, 1), 500);
        Pageable pageable = PageRequest.of(0, size, sort);

        long total = repo.countInBBox(west, south, east, north, year);

        List<HotspotItemDto> items = repo.findInBBox(west, south, east, north, year, pageable)
                .stream()
                .map(h -> new HotspotItemDto(
                        "HS_" + h.getId(),
                        h.getName(),
                        h.getLat(),
                        h.getLng(),
                        h.getAccidents(),
                        h.getCasualties(),
                        h.getStatYear()
                ))
                .toList();

        return new HotspotListDto(total, items);
    }
}
