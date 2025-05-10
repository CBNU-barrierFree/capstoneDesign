package com.example.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 사용자 ID(username)를 기준으로 사용자 정보를 조회 (로그인 시 사용)
    Optional<UserEntity> findByUsername(String username);

    // 닉네임을 기준으로 사용자 정보를 조회 (회원가입 시 중복 확인용)
    Optional<UserEntity> findByNickname(String nickname);
}

