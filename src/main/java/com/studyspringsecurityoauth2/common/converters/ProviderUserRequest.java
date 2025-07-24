package com.studyspringsecurityoauth2.common.converters;

import com.studyspringsecurityoauth2.model.users.User;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

// record
// 생성자, getters, toString(), equals(), hashCode() 자동 생성
// 불변 객체 (필드는 모두 final) -> 멀티스레드 환경에서 safe 한 객체
public record ProviderUserRequest(
        ClientRegistration clientRegistration,
        OAuth2User oAuth2User,
        User user
) {
    // OAuth2 login 할 때 사용하는 생성자
    public ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        this(clientRegistration, oAuth2User, null);
    }

    // form login 할 때 사용하는 생성자 (User 객체만 받게)
    public ProviderUserRequest(User user) {
        this(null, null, user);
    }

}
