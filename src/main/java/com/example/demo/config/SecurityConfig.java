package com.example.demo.config;

import com.example.demo.User.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 이 클래스가 Spring 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security 웹 보안을 활성화
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    // 생성자를 통한 의존성 주입
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean // SecurityFilterChain을 Bean으로 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (API 또는 간단한 웹앱에서 사용)
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화 (커스텀 폼 사용 예정)

                // URL 경로에 따른 접근 권한 설정
                .authorizeHttpRequests((requests) -> requests
                        // 정적 자원에 대한 접근 허용
                        .requestMatchers("/", "/css/**", "/script/**", "/images/**", "/static/**").permitAll()
                        // 회원가입 및 로그인 관련 경로는 인증 없이 접근 가능
                        .requestMatchers("/users/signup", "/users/login", "/users/signup_complete").permitAll()
                        // 관광지 관련 API 및 모든 URL에 대해 접근 허용 (최종 프로젝트 단계에서 필요에 따라 제한 예정)
                        .requestMatchers("/touristSpot/**", "/api/**", "/api/tourist-accessible-info", "/touristSpot/Json/**", "/**").permitAll()
                        // 게시판은 로그인 한 사람만 접근 허용
                        .requestMatchers("/posts/**").authenticated()
                        // 위에서 허용하지 않은 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 커스텀 로그인 설정
                .formLogin((form) -> form
                        .loginPage("/users/login") // 로그인 페이지 경로
                        .loginProcessingUrl("/users/login") // 로그인 처리 URL
                        .usernameParameter("username") // 로그인 폼의 사용자명 파라미터 이름
                        .passwordParameter("password") // 로그인 폼의 비밀번호 파라미터 이름
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // 로그인 성공 시 HTTP 200 응답
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 로그인 실패 시 HTTP 401 응답
                        })
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 이동할 기본 URL
                        .failureUrl("/users/login?error=true") // 로그인 실패 시 이동할 URL
                        .permitAll() // 로그인 관련 경로 접근 허용
                )

                // 로그아웃 설정
                .logout((logout) -> logout
                        .logoutUrl("/users/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/users/login?logout") // 로그아웃 성공 후 이동할 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll() // 로그아웃 관련 경로 접근 허용
                )

                // 사용자 정보를 불러오는 서비스 지정
                .userDetailsService(customUserDetailsService);

        return http.build(); // 설정을 기반으로 SecurityFilterChain 빌드
    }

    @Bean // 패스워드 인코더를 Bean으로 등록 (BCrypt 사용)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
