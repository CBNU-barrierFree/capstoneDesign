package com.example.demo.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

// UserEntity 기반으로 Spring Security의 UserDetails를 구현한 클래스
public class CustomUserDetails implements UserDetails {
    private final UserEntity user; // 실제 사용자 정보를 담고 있는 객체

    // 생성자 주입으로 UserEntity 받아서 저장
    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    // 사용자 닉네임 반환 (추가적인 사용자 정보가 필요할 경우 직접 정의 가능)
    public String getNickname() {
        return user.getNickname();
    }

    // 사용자 이름 반환
    public String getName() {
        return user.getName();
    }

    // 사용자의 권한 목록 반환 (여기서는 ROLE_ 접두어를 붙인 단일 권한 부여)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + user.getRole());
    }

    // 사용자 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 사용자 ID (username) 반환
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지 여부 (true: 만료 안 됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지 여부 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명이 만료되지 않았는지 여부 (true: 유효함)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되었는지 여부 (true: 활성 상태)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
