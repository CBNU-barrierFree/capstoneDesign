package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "my.custom")
public class MyCustomConfig {
    private String property;
}
