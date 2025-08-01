package com.studyspringsecurityoauth2.service;

import com.studyspringsecurityoauth2.common.converters.ProviderUserConverter;
import com.studyspringsecurityoauth2.common.converters.ProviderUserRequest;
import com.studyspringsecurityoauth2.model.PrincipalUser;
import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.repository.UserRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

// 1
@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    /*public CustomOAuth2UserService(UserService userService, UserRepository userRepository) {
        super(userService, userRepository);
    }*/

    public CustomOAuth2UserService(UserService userService, UserRepository userRepository, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userService, userRepository, providerUserConverter);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 2
        //ProviderUser providerUser = super.providerUser(clientRegistration, oAuth2User);
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oAuth2User);
        ProviderUser providerUser = providerUser(providerUserRequest);

        // 회원가입
        super.register(providerUser, userRequest);

        return new PrincipalUser(providerUser);
    }

}