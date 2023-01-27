// 2023-01-26
package com.example.todo.config;

import com.example.todo.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // 패스워드 인코딩 클래스를 등록     --> 패스워드 인코딩 클래스 : 내가 등록한 클래스가 아님
    @Bean
    public PasswordEncoder encoder() {      // BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정 (Postman 401 Unauthorized error)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 시큐리티 빌더
        http.cors()             // CORS(크로스오리진) 정책
                .and()
                .csrf()         // CSRF 정책
                .disable()     // 사용 안함 (너네가 만든 기본 보안정책을 내가 사용하지 않고, 자체적으로 만든것을 사용하겠다)
                .httpBasic().disable()      // 기본 시큐리티 인증 해제 (우리는 토큰 인증 사용할 것이기 때문)
                // 세션 기반 인증 안함 (우리는 토큰 인증 사용할 것이기 때문)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 인증 요청중에서 '/' 경로와 '/api/auth' 로 시작되는 경로는 인증하지 않고 모두 허용, 이외의 요청은 403 에러
                .authorizeRequests().antMatchers("/", "/api/auth/**").permitAll()
                // 그 외의 모든 경로는 인증을 거쳐야 함
                .anyRequest().authenticated();

        // 토큰 인증 필터 등록
        http.addFilterAfter(
                jwtAuthFilter,       //커스텀필터
                CorsFilter.class    // import 주의(spring)    : 누구 뒤에 붙일지
        );

        return http.build();
    }
}
