package com.studyspringsecurityoauth2.common.converters;

import com.studyspringsecurityoauth2.common.enums.OAuth2Config;
import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.model.social.GoogleUser;
import com.studyspringsecurityoauth2.common.util.OAuth2Utils;

public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if(! providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.GOOGLE.getSocialName())) {
            return null;
        }

        return new GoogleUser(
                OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }

}
