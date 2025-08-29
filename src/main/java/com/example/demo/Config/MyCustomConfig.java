package com.example.demo.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// application.properties에서 "my.custom"이라는 prefix로 시작하는 설정을 이 클래스에 바인딩
@ConfigurationProperties(prefix = "my.custom")
public class MyCustomConfig { // 설정값을 바인딩받기 위한 POJO 클래스
    private String property; // "my.custom.property" 항목의 값을 저장할 필드
}