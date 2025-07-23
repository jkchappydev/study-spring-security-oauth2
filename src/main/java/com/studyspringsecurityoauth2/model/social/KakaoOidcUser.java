package com.studyspringsecurityoauth2.model.social;

import com.studyspringsecurityoauth2.model.Attributes;
import com.studyspringsecurityoauth2.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoOidcUser extends OAuth2ProviderUser {

    // 참고 : https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#oidc-user-info
    // 카카오는 oauth2 랑 oidc 랑 사용자 정보를 가져오는 뎁스가 차이가 나서 별도로 구성해주어야 한다.
    public KakaoOidcUser(Attributes attributes, OAuth2User oauth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oauth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("nickname");
    }

    @Override
    public String getPicture() {
        return (String) getAttributes().get("profile_image_url");
    }

    @Override
    public OAuth2User getOAuth2User() {
        return super.getOAuth2User();
    }

}
