package com.studyspringsecurityoauth2.service;

import com.studyspringsecurityoauth2.model.*;
import com.studyspringsecurityoauth2.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

// 2
@Service
@Getter
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {

    private static final String REGISTRATION_GOOGLE = "google";
    private static final String REGISTRATION_NAVER = "naver";
    private static final String REGISTRATION_KEYCLOAK = "keycloak";

    // 5
    private final UserService userService;
    private final UserRepository userRepository;

    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        User user = userRepository.findByUsername(providerUser.getUsername());

        if (user == null) {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            userService.register(registrationId, providerUser);
        } else {
            System.out.println("user = " + user);
        }
    }

    protected ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        // 구글, 네이버, 키클록 구분
        String registrationId = clientRegistration.getRegistrationId();
        if (registrationId.equals(REGISTRATION_GOOGLE)) {
            return new GoogleUser(oAuth2User, clientRegistration);
        }

        if (registrationId.equals(REGISTRATION_NAVER)) {
            return new NaverUser(oAuth2User, clientRegistration);
        }

        if (registrationId.equals(REGISTRATION_KEYCLOAK)) {
            return new KeycloakUser(oAuth2User, clientRegistration);
        }

        return null;
    }

}