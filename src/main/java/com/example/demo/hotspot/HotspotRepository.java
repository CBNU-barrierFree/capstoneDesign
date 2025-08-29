package com.example.demo.hotspot;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HotspotRepository extends JpaRepository<HotspotEntity, Long> {

    @Query("""
      SELECT h FROM HotspotEntity h
      WHERE h.lng BETWEEN :west AND :east
        AND h.lat BETWEEN :south AND :north
        AND (:year IS NULL OR h.statYear = :year)
      """)
    List<HotspotEntity> findInBBox(
            @Param("west") double west,
            @Param("south") double south,
            @Param("east") double east,
            @Param("north") double north,
            @Param("year") Integer year
    );

    @Query("""
      SELECT COUNT(h) FROM HotspotEntity h
      WHERE h.lng BETWEEN :west AND :east
        AND h.lat BETWEEN :south AND :north
        AND (:year IS NULL OR h.statYear = :year)
      """)
    long countInBBox(
            @Param("west") double west,
            @Param("south") double south,
            @Param("east") double east,
            @Param("north") double north,
            @Param("year") Integer year
    );

    Optional<HotspotEntity> findFirstByStatYearAndLatAndLng(int statYear, double lat, double lng);
}
