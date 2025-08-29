package com.example.demo.Config;

import com.example.demo.User.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // ✅ 여기 두 줄이 핵심 (관리/조회 API 개방)
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/api/accident-hotspots/**").permitAll()

                        .requestMatchers("/posts/new", "/posts/delete/**").authenticated()
                        .requestMatchers("/", "/index.html", "/css/**", "/script/**", "/images/**",
                                "/static/**", "/posts/**", "/users/signup",
                                "/users/login", "/users/signup_complete",
                                "/touristSpot/**", "/api/tourist-accessible-info",
                                "/touristSpot/Json/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .loginProcessingUrl("/users/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            Object principal = authentication.getPrincipal();
                            String nickname = "";
                            if (principal instanceof com.example.demo.User.CustomUserDetails customUser) {
                                nickname = customUser.getNickname();
                                request.getSession().setAttribute("user", customUser.getUserEntity());
                                request.getSession().setAttribute("nickname", nickname);
                            }
                            Cookie nicknameCookie = new Cookie("nickname", nickname);
                            nicknameCookie.setPath("/");
                            nicknameCookie.setHttpOnly(false);
                            response.addCookie(nicknameCookie);

                            var savedRequest = (org.springframework.security.web.savedrequest.SavedRequest)
                                    request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                            response.sendRedirect(savedRequest != null ? savedRequest.getRedirectUrl() : "/");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                        .failureUrl("/users/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "nickname")
                        .permitAll()
                )
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
