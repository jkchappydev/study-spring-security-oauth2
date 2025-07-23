package com.studyspringsecurityoauth2.config;

import com.studyspringsecurityoauth2.common.authority.CustomAuthorityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

@Configuration
public class OAuth2AppConfig {

    // 9
    // 구글은 scope 가 profile, email 같이 딱 잘라지는게 아니라 http로 시작하는 아주 긴 문장으로 오기 때문에 잘라내야함
    @Bean
    public GrantedAuthoritiesMapper customAuthoritiesMapper() {
        return new CustomAuthorityMapper();
    }

}
