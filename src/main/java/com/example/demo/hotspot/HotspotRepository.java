package com.example.demo.hotspot;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
            @Param("year") Integer year,
            Pageable pageable
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
}
