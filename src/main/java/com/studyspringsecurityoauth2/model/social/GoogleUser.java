package com.studyspringsecurityoauth2.model.social;

import com.studyspringsecurityoauth2.model.Attributes;
import com.studyspringsecurityoauth2.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUser extends OAuth2ProviderUser {

    // OAuth2User : OAuth2 인증을 마친 후 반환하는 사용자 정보 객체.
    // ClientRegistration : 어떤 OAuth2 공급자(Google, Naver, Keycloak 등)인지 구분할 수 있게 해주는 클라이언트 메타 정보 객체.
    public GoogleUser(Attributes attributes, OAuth2User oauth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oauth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() { // 구글은 같음
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getPicture() {
        return null;
    }

    @Override
    public OAuth2User getOAuth2User() {
        return super.getOAuth2User();
    }

}
