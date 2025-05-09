package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration // 이 클래스가 Spring의 설정 클래스임을 명시
@EnableScheduling // 스케줄링 기능을 활성화 (예: @Scheduled 메서드를 주기적으로 실행할 수 있게 함)
public class SchedulingConfig {
    // 현재는 내용이 없지만, @Scheduled가 붙은 메서드가 동작하게 하기 위해 이 클래스와 애너테이션이 반드시 필요함

}