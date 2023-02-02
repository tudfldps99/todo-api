// 2023-01-31
package com.example.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // api cors 정책 설정
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000"
                        , "http://practice-s3-psy-bucket001.s3-website.ap-northeast-2.amazonaws.com"
                        , "http://todo-app-park-230201.s3-website.ap-northeast-2.amazonaws.com")        // api 요청 허용 URL (2023-02-01 추가)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // api 요청 허용 메소드
                .allowedHeaders("*")
                .allowCredentials(true)     // 자격증명
                .maxAge(3600);      // (초 단위) - 1시간
    }
}
