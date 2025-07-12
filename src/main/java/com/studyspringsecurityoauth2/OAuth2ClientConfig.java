package com.studyspringsecurityoauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(keycloakRegistration());
    }

    private ClientRegistration keycloakRegistration() {
        return ClientRegistrations.fromIssuerLocation("http://localhost:8080/realms/oauth2")
                // client
                .registrationId("keycloak")
                .clientId("oauth2-client-app")
                .clientSecret("AoMzGoVdOy0a0nykBpEvFtUpTfJjzfXM")
                .redirectUri("http://localhost:8081/login/oauth2/code/keycloak")
                // provider
                // fromIssuerLocation() 에서 전달하고 있으므로 생각 가능
                // .issuerUri("http://localhost:8080/realms/oauth2")
                .build();
    }

}
