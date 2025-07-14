package com.studyspringsecurityoauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 표준 방식
    @GetMapping("/user")
    public OAuth2User user(Authentication auth) {
        // 직접 SecurityContext 로 부터 인증객체를 가져온다.
        // OAuth2AuthenticationToken authentication1 = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        // 메서드에 선언된 참조를 가져온다.
        OAuth2AuthenticationToken authentication2 = (OAuth2AuthenticationToken) auth;
        return authentication2.getPrincipal();
    }

    // 어노테이션 방식
    @GetMapping("/oauth2User")
    public OAuth2User oAuth2User(@AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("oAuth2User = " + oAuth2User);
        return oAuth2User;
    }

    // oidc 방식
    // OidcUser 는 OAuth2User 를 상속받기 때문에, OAuth2User 로도 참조가 가능하지만 명확하게 작성하는 것이 좋다.
    @GetMapping("/oidcUser")
    public OidcUser oidcUser(@AuthenticationPrincipal OidcUser oidcUser) {
        System.out.println("oAuth2User = " + oidcUser);
        return oidcUser;
    }

}