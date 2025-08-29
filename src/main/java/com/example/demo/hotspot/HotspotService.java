package com.example.demo.hotspot;

import com.example.demo.hotspot.dto.HotspotItemDto;
import com.example.demo.hotspot.dto.HotspotListDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotspotService {

    private final HotspotRepository repo;

    public HotspotService(HotspotRepository repo) { this.repo = repo; }

    public HotspotListDto searchInBBox(double west, double south, double east, double north,
                                       Integer year, String order, Integer limit) {

        List<HotspotEntity> raw = repo.findInBBox(west, south, east, north, year);
        long total = raw.size();

        Comparator<HotspotEntity> cmp =
                "casualties".equalsIgnoreCase(order)
                        ? Comparator.comparingInt(HotspotEntity::getCasualties).reversed()
                        : Comparator.comparingInt(HotspotEntity::getAccidents).reversed();

        List<HotspotItemDto> items = raw.stream()
                .sorted(cmp)
                .limit(limit == null ? 200 : limit)
                .map(h -> new HotspotItemDto(
                        h.getId(), h.getName(), h.getLat(), h.getLng(),
                        h.getAccidents(), h.getCasualties(), h.getStatYear()))
                .collect(Collectors.toList());

        return new HotspotListDto(total, items);
    }
}
