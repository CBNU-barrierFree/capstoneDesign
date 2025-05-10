package com.example.demo.Tour;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // 이 클래스가 JPA 엔티티임을 명시
@Getter // Lombok을 이용해 모든 필드의 getter 자동 생성
@Setter // Lombok을 이용해 모든 필드의 setter 자동 생성
@Table(name = "tour_entity") // 이 엔티티가 매핑될 테이블 이름 지정
public class TourEntity {

    @Id // 기본 키 필드임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 전략 (MySQL에서 주로 사용)
    private Long id; // 관광지 고유 ID (자동 생성)

    private String title; // 제목
    private String issuedDate; // 발행일
    private String category1; // 카테고리1
    private String category2; // 카테고리2
    private String category3; // 카테고리3

    @Column(columnDefinition = "TEXT") // 길이가 긴 설명 저장을 위한 TEXT 타입 지정
    private String description; // 상세 설명

    @Column(columnDefinition = "TEXT") // 길이가 긴 부가 설명 저장
    private String subDescription; // 부가 설명

    private String tel; // 전화번호

    @Column(length = 2048) // URL은 길어질 수 있으므로 길이를 늘림
    private String url; // 관련 URL

    @Column(columnDefinition = "TEXT") // 주소가 길 수 있으므로 TEXT 타입
    private String address; // 주소

    @Column(columnDefinition = "TEXT") // 좌표 정보도 길어질 수 있어 TEXT 타입
    private String coordinates; // 좌표 정보
}