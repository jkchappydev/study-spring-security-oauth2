package com.studyspringsecurityoauth2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class ClientController {

    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public ClientController(OAuth2AuthorizedClientRepository authorizedClientRepository, OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientRepository = authorizedClientRepository;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/client")
    public String client(HttpServletRequest request, Model model) {
        // 여기까지 왔다는 건, 클라이언트가 인가를 받고 access token 까지 받았다는 의미
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String clientRegistrationId = "keycloak";

        // 1. repository 에서 가져오기
        OAuth2AuthorizedClient authorizedClient1 = authorizedClientRepository
                .loadAuthorizedClient(clientRegistrationId, authentication, request);

        // 사용자 인증 처리 하기
        OAuth2AccessToken accessToken = authorizedClient1.getAccessToken();

        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService
                .loadUser(new OAuth2UserRequest(authorizedClient1.getClientRegistration(), accessToken));

        OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(
                oAuth2User, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")), clientRegistrationId
        ); // 최종 사용자 인증 객체

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 시큐리티 컨텍스트에 담음

        model.addAttribute("accessToken", accessToken.getTokenValue());
        model.addAttribute("refreshToken", authorizedClient1.getRefreshToken().getTokenValue());
        model.addAttribute("principalName", oAuth2User.getName());
        model.addAttribute("clientName", authorizedClient1.getClientRegistration().getClientName());

        // 2. service 에서 가져오기
        OAuth2AuthorizedClient authorizedClient2 = authorizedClientService
                .loadAuthorizedClient(clientRegistrationId, authentication.getName());

        return "/client";
    }

}
