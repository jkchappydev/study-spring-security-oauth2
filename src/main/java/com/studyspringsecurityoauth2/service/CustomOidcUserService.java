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
        // ClientRegistration clientRegistration = userRequest.getClientRegistration();

        // OAuth2 공급자마다 사용자 고유 식별자의 키 이름이 다르기 때문에 작성
        // ClientRegistration 의 속성을 다 가져온 다음 추가적으로 속성을 설정 할 수 있음.
        // oidc 는 기본적으로 "sub" 사용함
        ClientRegistration clientRegistration = ClientRegistration.withClientRegistration(userRequest.getClientRegistration())
                .userNameAttributeName("sub") // 이 필드가 getName()의 기준이 됨
                .build();

        // OidcUserRequest 를 재정의 해야한다. (위에서 변경된 속성을 가지는 clientRegistration 이 아닌 이전 상태를 가지고 있기 때문에)
        OidcUserRequest oidcUserRequest = new OidcUserRequest(
                clientRegistration,
                userRequest.getAccessToken(),
                userRequest.getIdToken(),
                userRequest.getAdditionalParameters()
        );

        // OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = new OidcUserService();
        OidcUserService oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);

        // 2
        // ProviderUser providerUser = super.providerUser(clientRegistration, oidcUser);
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);
        ProviderUser providerUser = super.providerUser(providerUserRequest);

        // 회원가입
        super.register(providerUser, oidcUserRequest);

        return new PrincipalUser(providerUser);
    }

}
