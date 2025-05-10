package com.example.demo.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // 이 클래스가 JPA 엔티티임을 명시
@Getter // Lombok: 모든 필드에 대한 getter 자동 생성
@Setter // Lombok: 모든 필드에 대한 setter 자동 생성
@Table(name = "users_data") // 매핑될 테이블명을 "users_data"로 지정
public class UserEntity {

    @Id // 기본 키(primary key) 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT 방식 사용
    private Long id; // 사용자 고유 ID

    @Column(nullable = false) // null 허용하지 않음
    private String name; // 사용자 이름

    @Column(nullable = false, unique = true) // 중복 불가, null 불가
    private String username; // 로그인에 사용할 사용자 ID

    @Column(nullable = false) // null 허용하지 않음
    private String password; // 암호화된 비밀번호

    @Column(nullable = false, unique = true) // 중복 불가, null 불가
    private String nickname; // 닉네임 (유저 구분용 표시명)

    private String role = "USER"; // 사용자 권한 (기본값은 "USER")
}
