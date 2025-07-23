package com.studyspringsecurityoauth2.common.util;

import com.studyspringsecurityoauth2.common.enums.OAuth2Config;
import com.studyspringsecurityoauth2.model.Attributes;
import com.studyspringsecurityoauth2.model.PrincipalUser;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class OAuth2Utils {

    public static Attributes getMainAttributes(OAuth2User oAuth2User) {

        return Attributes.builder()
                .mainAttributes(oAuth2User.getAttributes()) // OAuth2User 는 attributes 라는 사용자 속성을 가지고 있다.
                .build();
    }

    public static Attributes getSubAttributes(OAuth2User oAuth2User, String subAttributesKey) {
        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        return Attributes.builder()
                .subAttributes(subAttributes)
                .build();
    }

    public static Attributes getOtherAttributes(OAuth2User oAuth2User, String subAttributesKey, String otherAttributesKey) {
        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        Map<String, Object> otherAttributes = (Map<String, Object>) subAttributes.get(otherAttributesKey);

        return Attributes.builder()
                .subAttributes(subAttributes)
                .otherAttributes(otherAttributes)
                .build();
    }

    // IndexController 참고
    /*public static String oAuth2UserName(OAuth2AuthenticationToken authenticationToken, PrincipalUser principalUser) {
        String userName;
        String registrationId = authenticationToken.getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = principalUser.providerUser().getOAuth2User();

        // google, facebook, apple
        Attributes attributes = OAuth2Utils.getMainAttributes(oAuth2User);
        userName = (String) attributes.getMainAttributes().get("name");

        // naver
        if (registrationId.equals(OAuth2Config.SocialType.NAVER.getSocialName())) {
            attributes = OAuth2Utils.getSubAttributes(oAuth2User, "response");
            userName = (String) attributes.getSubAttributes().get("name");
        } else if (registrationId.equals(OAuth2Config.SocialType.KAKAO.getSocialName())) { // kakao
            // OpenID Connect (카카오 지원o)
            if (oAuth2User instanceof OidcUser) {
                attributes = OAuth2Utils.getMainAttributes(oAuth2User);
                userName = (String) attributes.getMainAttributes().get("nickname");
            } else {
                attributes = OAuth2Utils.getOtherAttributes(principalUser, "profile", null);
                userName = (String) attributes.getSubAttributes().get("nickname");
            }
        }

        return userName;
    }*/

}
