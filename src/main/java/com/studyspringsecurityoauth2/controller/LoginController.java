package com.studyspringsecurityoauth2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    @GetMapping("/oauth2Login")
    public String oauth2Login(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 현재 인증된 사용자의 Authentication 객체 가져오기
        // client_credentials 방식에서는 일반적으로 익명 인증 또는 시스템 계정 사용
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 클라이언트가 권한부여 요청했을 때, 실제 인가 처리
        // OAuth2 인가 요청 객체 생성
        // client_registration_id는 application.yml 또는 clientRegistrationRepository에 등록된 클라이언트 식별자
        // client_credentials 방식은 사용자 로그인 없이 시스템이 직접 토큰 요청
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal(authentication) // client_credentials일 경우 보통 'anonymous' 또는 시스템 principal
                .attribute(HttpServletRequest.class.getName(), request) // 요청 객체를 attribute에 포함 (필수)
                .attribute(HttpServletResponse.class.getName(), response) // 응답 객체를 attribute에 포함 (필수)
                .build();

        // 인가 성공 시 처리할 커스텀 핸들러 등록
        // 기본적으로 access token 이 발급되면 authorizedClientRepository 에 저장
        OAuth2AuthorizationSuccessHandler successHandler = (authorizedClient, principal, attributes) -> {
            authorizedClientRepository.saveAuthorizedClient(
                    authorizedClient,
                    principal,
                    (HttpServletRequest) attributes.get(HttpServletRequest.class.getName()),
                    (HttpServletResponse) attributes.get(HttpServletResponse.class.getName())
            );
            System.out.println("authorizedClient = " + authorizedClient);
            System.out.println("principal = " + principal);
            System.out.println("attributes = " + attributes);
        };

        authorizedClientManager.setAuthorizationSuccessHandler(successHandler);

        // authorize(): 실제로 access token 요청 및 저장소 저장까지 진행
        // client_credentials 방식에서는 사용자 인증 없이 클라이언트 자격(client_id + secret)으로 access token 발급
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        model.addAttribute("authorizedClient", authorizedClient.getAccessToken().getTokenValue());

        return "home";
    }

    @GetMapping("/logout")
    public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        return "redirect:/";
    }

}
