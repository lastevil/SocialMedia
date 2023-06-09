package com.socialmedia.example.configs;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
@PropertySource("classpath:/application.yaml")
@PropertySource("classpath:/secrets.properties")

public class AppConfig {
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(3));
        factory.setMaxRequestSize(DataSize.ofMegabytes(3));
        return factory.createMultipartConfig();
    }
}
