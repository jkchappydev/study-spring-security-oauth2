package com.cors2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.
                authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated())
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 모든 도메인에서의 요청을 허용
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드(GET, POST 등)를 허용
        configuration.addAllowedHeader("*"); // 모든 요청 헤더를 허용
        // configuration.setAllowCredentials(true); // 인증 정보(Cookie, Authorization 등) 포함 허용 (true를 줄 경우, addAllowedOrigin에는 와일드카드 사용 불가능)
        configuration.setMaxAge(3600L); // preflight 결과를 3600초(1시간) 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용 ("/api/**" 이면 /api/로 시작하는 모든 요청에만 CORS 적용)

        return source;
    }

}
