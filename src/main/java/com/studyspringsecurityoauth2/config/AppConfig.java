package com.studyspringsecurityoauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class AppConfig {

    @Bean
    public OAuth2AuthorizedClientManager auth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository, // 클라이언트 등록 정보 저장소
            OAuth2AuthorizedClientRepository authorizedClientRepository // 인증된 클라이언트 저장소
    ) {
        // 사용할 OAuth2 인증 방식들을 구성
        OAuth2AuthorizedClientProvider auth2AuthorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode() // 권장 방식: Authorization Code Grant
                        // .password() - Spring Security 5.8부터 deprecated
                        .clientCredentials() // 클라이언트 자격 증명 방식 (서버 간 통신 등에서 사용)
                        .refreshToken() // Refresh Token 을 통한 재인증 지원
                        .build();

        // DefaultOAuth2AuthorizedClientManager 생성 (인증 클라이언트를 관리)
        DefaultOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

        // 위에서 구성한 provider 를 설정
        oAuth2AuthorizedClientManager.setAuthorizedClientProvider(auth2AuthorizedClientProvider);

        return oAuth2AuthorizedClientManager;
    }

}
