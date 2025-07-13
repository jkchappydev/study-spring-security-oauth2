package com.studyspringsecurityoauth2;

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
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                //        .requestMatchers("/loginPage").permitAll()
                        .anyRequest().authenticated())
                // .oauth2Login(oauth2 -> oauth2.loginPage("/loginPage")); // 커스텀 로그인 페이지
                .oauth2Login(Customizer.withDefaults()); // 디폴트 로그인 페이지

        return http.build();
    }

}
