package com.example.demo.Tour;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA 엔티티임을 알려줌
@Getter // Lombok을 이용해 모든 필드의 getter 자동 생성
@Setter // Lombok을 이용해 모든 필드의 setter 자동 생성
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 생성자 추가
@Table(name = "tour_entity") // 이 엔티티클래스가 매핑될 테이블 이름 지정
public class TourEntity {

    @Id // 기본 키 필드임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 제목
    private String issuedDate; // 발행일
    private String category1; // 카테고리1
    private String category2; // 카테고리2
    private String category3; // 카테고리3

    @Column(columnDefinition = "TEXT") // 길이가 긴 설명 저장을 위한 TEXT 타입
    private String description; // 상세 설명

    @Column(columnDefinition = "TEXT") // 길이가 긴 부가 설명 저장
    private String subDescription; // 부가 설명

    private String tel; // 전화번호

    @Column(length = 2048) // URL은 길어질 수 있으므로 길이를 임의로 늘림
    private String url; // 관련 URL

    @Column(columnDefinition = "TEXT") // 주소가 길 수 있어서 TEXT 타입
    private String address; // 주소

    @Column(columnDefinition = "TEXT") // 좌표 정보도 길어질 수 있어서 TEXT 타입
    private String coordinates; // 좌표 정보
}