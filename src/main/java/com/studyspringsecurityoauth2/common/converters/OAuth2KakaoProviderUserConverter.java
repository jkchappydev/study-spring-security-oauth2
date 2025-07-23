package com.studyspringsecurityoauth2.common.converters;

import com.studyspringsecurityoauth2.common.enums.OAuth2Config;
import com.studyspringsecurityoauth2.common.util.OAuth2Utils;
import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.model.social.KakaoUser;
import com.studyspringsecurityoauth2.model.social.NaverUser;

public class OAuth2KakaoProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if(!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.KAKAO.getSocialName())) {
            return null;
        }

        return new KakaoUser(
                OAuth2Utils.getOtherAttributes(providerUserRequest.oAuth2User(), "kakao_account", "profile"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }

}
