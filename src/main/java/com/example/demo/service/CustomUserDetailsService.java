package com.example.demo.service;

import com.example.demo.config.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service // 이 클래스가 서비스 계층의 컴포넌트임을 나타내며, Spring이 자동으로 빈으로 등록
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 사용자 정보를 조회하기 위한 리포지토리

    // 생성자를 통한 의존성 주입
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 이름(username)으로 사용자 정보를 로드하는 메서드 (Spring Security에서 호출함)
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // username으로 사용자 조회, 없으면 예외 발생
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 조회된 UserEntity를 CustomUserDetails로 감싸서 반환 (Spring Security가 사용할 수 있게 함)
        return new CustomUserDetails(user);
    }
}