package com.example.demo.Config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration // 이 클래스가 Spring 설정 클래스임을 나타냄
@EnableConfigurationProperties(MyCustomConfig.class) // MyCustomConfig에 @ConfigurationProperties를 활성화하여 바인딩 가능하게 함
public class AppConfig {

    @Bean // RestTemplate을 빈으로 등록하여 의존성 주입 가능하게 함
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(); // RestTemplate 객체 생성
        restTemplate.getMessageConverters().add(new MappingJackson2XmlHttpMessageConverter()); // XML 형식의 응답을 처리할 수 있도록 메시지 컨버터 추가
        return restTemplate; // 구성된 RestTemplate 반환
    }
}