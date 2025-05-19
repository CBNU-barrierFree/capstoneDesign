package com.example.demo.Tour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository // Spring의 Repository 컴포넌트임을 알려줌 (빈으로 등록)
public interface TourRepository extends JpaRepository<TourEntity, Long> {
    // title과 address가 모두 일치하는 관광지를 조회 (중복 체크용)
    Optional<TourEntity> findByTitleAndAddress(String title, String address);
    // JPQL을 사용한 커스텀 쿼리: 여러 필드를 기준으로 중복 관광지 조회
    @Query("SELECT t FROM TourEntity t WHERE t.title = :title AND t.address = :address " +
            "AND t.category1 = :category1 AND t.category2 = :category2 " +
            "AND t.category3 = :category3 AND t.coordinates = :coordinates")
    Optional<TourEntity> findDuplicate(
            // api data type들 기준으로 작성
            @Param("title") String title, // 제목
            @Param("address") String address, // 주소
            @Param("category1") String category1, // 카테고리1
            @Param("category2") String category2, // 카테고리2
            @Param("category3") String category3, // 카테고리3
            @Param("coordinates") String coordinates // 좌표
    );
}