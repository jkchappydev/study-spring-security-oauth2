package com.studyspringsecurityoauth2.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String DEFAULT_FILTER_PROCESSING_URI = "/oauth2Login/**";
    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private Duration clockSkew = Duration.ofSeconds(3600L);
    private Clock clock = Clock.systemUTC();

    private final OAuth2AuthorizationSuccessHandler successHandler;

    public CustomOAuth2AuthenticationFilter(DefaultOAuth2AuthorizedClientManager authorizedClientManager, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        // super(defaultFilterProcessesUrl); // defaultFilterProcessesUrl 은 해당 필터가 동작하기 위해 매칭 될 url 패턴을 정의 (매칭결과 참이면 Custom필터가 동작)
        super(DEFAULT_FILTER_PROCESSING_URI);
        this.authorizedClientManager = authorizedClientManager;
        this.authorizedClientRepository = authorizedClientRepository;

        // OAuth2AuthorizationSuccessHandler 는 생성자에서 처리하도록
        this.successHandler = (authorizedClient, principal, attributes) -> {
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
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 현재 인증된 사용자의 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            authentication =
                    new AnonymousAuthenticationToken("anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        }

        // 클라이언트가 권한부여 요청했을 때, 실제 인가 처리
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal(authentication)
                .attribute(HttpServletRequest.class.getName(), request) // 요청 객체를 attribute에 포함 (필수)
                .attribute(HttpServletResponse.class.getName(), response) // 응답 객체를 attribute에 포함 (필수)
                .build();

        // authorize(): 실제로 access token 요청 및 저장소 저장까지 진행
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient != null) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put(HttpServletRequest.class.getName(), request);
            attributes.put(HttpServletResponse.class.getName(), response);

            this.successHandler.onAuthorizationSuccess(authorizedClient, authentication, attributes);

            // 새롭게 인증된 사용자 정보 생성 (예: access token 기반)
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
            OAuth2User oauth2User = oAuth2UserService.loadUser(new OAuth2UserRequest(
                    authorizedClient.getClientRegistration(), accessToken));

            OAuth2AuthenticationToken authResult = new OAuth2AuthenticationToken(
                    oauth2User,
                    authentication.getAuthorities(),
                    authorizedClient.getClientRegistration().getRegistrationId()
            );

            return authResult;
        }

        throw new BadCredentialsException("Authorization failed");
    }

    private boolean hasTokenExpired(OAuth2Token token) {
        return this.clock.instant().isAfter(token.getExpiresAt().minus(this.clockSkew));
    }

}
