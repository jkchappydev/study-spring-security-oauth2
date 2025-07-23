package com.studyspringsecurityoauth2.common.converters;

import com.studyspringsecurityoauth2.common.enums.OAuth2Config;
import com.studyspringsecurityoauth2.common.util.OAuth2Utils;
import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.model.social.KakaoOidcUser;
import com.studyspringsecurityoauth2.model.social.KakaoUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class OAuth2KakaoOidcProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if(!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.KAKAO.getSocialName())) {
            return null;
        }

        // 카카오인 것 중에서도 Oidc인 것과 아닌 것을 구분해 주어야 함
        if(!(providerUserRequest.oAuth2User() instanceof OidcUser)) {
            return null;
        }

        return new KakaoOidcUser(
                OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }

}
