package com.example.demo.Config;

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
import jakarta.servlet.http.Cookie;

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
                        .requestMatchers("/posts/new").authenticated() // 게시판에 접근할 때는 인증 필요
                        .requestMatchers("/", "/css/**", "/script/**", "/images/**", "/static/**", "/posts/**").permitAll()
                        .requestMatchers("/users/signup", "/users/login", "/users/signup_complete").permitAll()
                        .requestMatchers("/touristSpot/**", "/api/**", "/api/tourist-accessible-info", "/touristSpot/Json/**", "/**").permitAll()
                        .anyRequest().authenticated() // 그 외는 인증 필요
                )

                // 커스텀 로그인 설정
                .formLogin((form) -> form
                        .loginPage("/users/login")
                        .loginProcessingUrl("/users/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            Object principal = authentication.getPrincipal();
                            String nickname = "";

                            if (principal instanceof com.example.demo.User.CustomUserDetails customUser) {
                                nickname = customUser.getNickname();
                                request.getSession().setAttribute("user", customUser.getUserEntity()); // ✅ 이 줄 추가!!
                                request.getSession().setAttribute("nickname", nickname);              // 선택 사항
                            }

                            Cookie nicknameCookie = new Cookie("nickname", nickname);
                            nicknameCookie.setPath("/");
                            nicknameCookie.setHttpOnly(false);
                            response.addCookie(nicknameCookie);

                            var savedRequest = (org.springframework.security.web.savedrequest.SavedRequest)
                                    request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                            if (savedRequest != null) {
                                response.sendRedirect(savedRequest.getRedirectUrl());
                            } else {
                                response.sendRedirect("/");
                            }
                        })

                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                        .failureUrl("/users/login?error=true")
                        .permitAll()
                )


                // 로그아웃 설정
                .logout((logout) -> logout
                        .logoutUrl("/users/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID", "nickname") // 쿠키 삭제
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
