package com.studyspringsecurityoauth2.converters;

import com.studyspringsecurityoauth2.converters.enums.OAuth2Config;
import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.model.social.NaverUser;
import com.studyspringsecurityoauth2.util.OAuth2Utils;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if(!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SoclaiType.NAVER.getSocialName())) {
            return null;
        }

        return new NaverUser(
                OAuth2Utils.getSubAttributes(providerUserRequest.oAuth2User(), "response"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }

}
