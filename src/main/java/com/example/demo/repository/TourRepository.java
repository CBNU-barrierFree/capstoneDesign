package com.example.demo.repository;

import com.example.demo.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<TourEntity, Long> {
    Optional<TourEntity> findByTitleAndAddress(String title, String address);
    @Query("SELECT t FROM TourEntity t WHERE t.title = :title AND t.address = :address " +
            "AND t.category1 = :category1 AND t.category2 = :category2 " +
            "AND t.category3 = :category3 AND t.coordinates = :coordinates")
    Optional<TourEntity> findDuplicate(
            @Param("title") String title,
            @Param("address") String address,
            @Param("category1") String category1,
            @Param("category2") String category2,
            @Param("category3") String category3,
            @Param("coordinates") String coordinates
    );

}

