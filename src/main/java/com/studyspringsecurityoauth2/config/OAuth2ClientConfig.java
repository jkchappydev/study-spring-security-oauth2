package com.studyspringsecurityoauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class OAuth2ClientConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/","/oauth2Login","/client").permitAll()
                        .anyRequest().authenticated())
                // 스프링 시큐리티 6은 5에 비해서 더 엄격한 규칙 적용 (따라서 이렇게 명시해야함)
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/client")
                )
                .oauth2Client(Customizer.withDefaults())
        ;

        return http.build();
    }

}
