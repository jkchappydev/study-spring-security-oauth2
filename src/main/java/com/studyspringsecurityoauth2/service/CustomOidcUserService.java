package com.studyspringsecurityoauth2.service;

import com.studyspringsecurityoauth2.common.converters.ProviderUserConverter;
import com.studyspringsecurityoauth2.common.converters.ProviderUserRequest;
import com.studyspringsecurityoauth2.model.PrincipalUser;
import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.repository.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

// 6
@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    /*public CustomOidcUserService(UserService userService, UserRepository userRepository) {
        super(userService, userRepository);
    }*/

    public CustomOidcUserService(UserService userService, UserRepository userRepository, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userService, userRepository, providerUserConverter);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        // OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = new OidcUserService();
        OidcUserService oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);

        // 2
        // ProviderUser providerUser = super.providerUser(clientRegistration, oidcUser);
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);
        ProviderUser providerUser = super.providerUser(providerUserRequest);

        // 회원가입
        super.register(providerUser, userRequest);

        return new PrincipalUser(providerUser);
    }

}
