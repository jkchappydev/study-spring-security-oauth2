package com.studyspringsecurityoauth2.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

// 각각의 서비스마다 반환 하는 UserDetails, OAuth2User, OidcUser 반환 타입을 동시에 처리하는 클래스
public record PrincipalUser(ProviderUser providerUser) implements UserDetails, OAuth2User, OidcUser {

    @Override
    public String getName() {
        return providerUser.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return providerUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return providerUser.getAuthorities();
    }

    @Override
    public String getPassword() {
        return providerUser.getPassword();
    }

    @Override
    public String getUsername() {
        return providerUser.getUsername();
    }

    // 여기서 부터는 일단 null (현재 사용하지 않음)
    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

}
