package com.studyspringsecurityoauth2.controller;

<<<<<<< HEAD
import ch.qos.logback.core.net.server.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    private Duration clockSkew = Duration.ofSeconds(3600L);

    private Clock clock = Clock.systemUTC();

    @GetMapping("/oauth2Login")
    public String oauth2Login(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 현재 인증된 사용자의 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 클라이언트가 권한부여 요청했을 때, 실제 인가 처리
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal(authentication)
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
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        // 1. 권한 부여 타입을 변경하지 않고 실행 (제일 간단)
        // 여기서도 access token 이 만료되면 자동적으로 refresh token을 이용해서 재발급함

        /*if (authorizedClient != null &&
                hasTokenExpired(authorizedClient.getAccessToken()) &&
                authorizedClient.getRefreshToken() != null) {
            authorizedClientManager.authorize(authorizeRequest);
        }*/

        // 2. 권한 부여 타입을 변경하고 실행
        // 수동적으로 authorization_code 방식의 요청을 refresh token 방식으로 변경 후 실행
        // 여기서는 조금 더 섬세한 설정이 가능하다.
        if (authorizedClient != null &&
                hasTokenExpired(authorizedClient.getAccessToken()) &&
                authorizedClient.getRefreshToken() != null) {

            // ClientRegistration 재정의
            ClientRegistration clientRegistration = ClientRegistration
                    .withClientRegistration(authorizedClient.getClientRegistration())
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .build();

            // OAuth2AuthorizedClient 재정의
            OAuth2AuthorizedClient oAuth2AuthorizedClient = new OAuth2AuthorizedClient(
                    clientRegistration, authorizedClient.getPrincipalName(), authorizedClient.getAccessToken(), authorizedClient.getRefreshToken()
            );

            // OAuth2AuthorizeRequest 재정의
            OAuth2AuthorizeRequest authorizeRequest2 = OAuth2AuthorizeRequest
                    .withAuthorizedClient(oAuth2AuthorizedClient)
                    .principal(authentication)
                    .attribute(HttpServletRequest.class.getName(), request) // 요청 객체를 attribute에 포함 (필수)
                    .attribute(HttpServletResponse.class.getName(), response) // 응답 객체를 attribute에 포함 (필수)
                    .build();

            authorizedClientManager.authorize(authorizeRequest2);
        }

        model.addAttribute("AccessToken", authorizedClient.getAccessToken().getTokenValue());
        if(authorizedClient.getRefreshToken() != null) { // client credentials 방식은 refresh token 미발급
            model.addAttribute("RefreshToken", authorizedClient.getRefreshToken().getTokenValue());
        }

        return "home";
    }

    @GetMapping("/v2/oauth2Login")
    public String oauth2LoginV2(@RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient, Model model) {
        /*// 현재 인증된 사용자의 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 클라이언트가 권한부여 요청했을 때, 실제 인가 처리
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal(authentication)
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
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient != null &&
                hasTokenExpired(authorizedClient.getAccessToken()) &&
                authorizedClient.getRefreshToken() != null) {
            authorizedClientManager.authorize(authorizeRequest);
        }*/

        // =================================================================================================
        // 위의 모든 작업이 @RegisteredOAuth2AuthorizedClient 으로 해결됨
        if (authorizedClient != null) {
            // 여기서 사용 안함
            // Map<String, Object> attributes = new HashMap<>();
            // attributes.put(HttpServletRequest.class.getName(), request);
            // attributes.put(HttpServletResponse.class.getName(), response);

            // this.successHandler.onAuthorizationSuccess(authorizedClient, authentication, attributes);

            // 새롭게 인증된 사용자 정보 생성 (예: access token 기반)
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
            OAuth2User oauth2User = oAuth2UserService.loadUser(new OAuth2UserRequest(
                    authorizedClient.getClientRegistration(), accessToken));

            SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
            authorityMapper.setPrefix("SYSTEM_");
            Set<GrantedAuthority> grantedAuthorities = authorityMapper.mapAuthorities(oauth2User.getAuthorities());

            OAuth2AuthenticationToken authResult = new OAuth2AuthenticationToken(
                    oauth2User,
                    grantedAuthorities,
                    authorizedClient.getClientRegistration().getRegistrationId()
            );

            model.addAttribute("oAuth2AuthenticationToken", authResult);
            model.addAttribute("AccessToken", authorizedClient.getAccessToken().getTokenValue());
            if(authorizedClient.getRefreshToken() != null) { // client credentials 방식은 refresh token 미발급
                model.addAttribute("RefreshToken", authorizedClient.getRefreshToken().getTokenValue());
            }
        }

        return "home";
    }

    @GetMapping("/logout")
    public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        return "redirect:/";
    }

    private boolean hasTokenExpired(OAuth2Token token) {
        return this.clock.instant().isAfter(token.getExpiresAt().minus(this.clockSkew));
=======
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        return "redirect:/login";
>>>>>>> study/section10
    }

}
