package com.studyspringsecurityoauth2.model.social;

import com.studyspringsecurityoauth2.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KeycloakUser extends OAuth2ProviderUser {

    public KeycloakUser(OAuth2User oauth2User, ClientRegistration clientRegistration) {
        super(oauth2User.getAttributes(), oauth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("preferred_username");
    }

}
