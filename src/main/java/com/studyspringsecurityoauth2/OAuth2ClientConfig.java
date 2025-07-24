package com.studyspringsecurityoauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
=======
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

>>>>>>> study/section7
@Configuration
public class OAuth2ClientConfig {

    @Bean
<<<<<<< HEAD
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults());

        return http.build();
=======
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
>>>>>>> study/section7
    }

}
=======
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class OAuth2ClientConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2ClientConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/home").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestResolver(customOAuth2AuthorizationRequestResolver())
                        )
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/home")
                )
        ;

        return http.build();
    }

    @Bean
    public OAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver() {
        return new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

}
>>>>>>> study/section8
