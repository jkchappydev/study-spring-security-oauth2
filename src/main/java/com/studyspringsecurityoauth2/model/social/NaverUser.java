package com.studyspringsecurityoauth2.model.social;

import com.studyspringsecurityoauth2.model.Attributes;
import com.studyspringsecurityoauth2.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverUser extends OAuth2ProviderUser {

    // 참고 : https://developers.naver.com/docs/login/devguide/devguide.md#3-4-%EB%84%A4%EC%9D%B4%EB%B2%84-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%97%B0%EB%8F%99-%EA%B0%9C%EB%B0%9C%ED%95%98%EA%B8%B0
    public NaverUser(Attributes response, OAuth2User oauth2User, ClientRegistration clientRegistration) {
        super(response.getSubAttributes(), oauth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }

}