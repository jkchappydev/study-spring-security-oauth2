package com.studyspringsecurityoauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .anyRequest().authenticated());
        http.formLogin(formLogin -> {});
        // spring security 6.2 부터는 두번째 인자로 Configurer customizer (람다식) 요구 (첫번째 Configurer 인스턴스)
        // http.with(new CustomSecurityConfigurer(), config -> config.setFlag(false)); // with() 사용: apply()는 spring security 6.2에서 deprecated 됨
        return http.build();
    }

    @Bean
    SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .anyRequest().authenticated());
        http.httpBasic(httpBasic -> {});
        return http.build();
    }

}
