package com.studyspringsecurityoauth2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private static final Consumer<OAuth2AuthorizationRequest.Builder> DEFAULT_PKCE_APPLIER = OAuth2AuthorizationRequestCustomizers.withPkce();
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RequestMatcher authorizationRequestMatcher;
    private DefaultOAuth2AuthorizationRequestResolver defaultOAuth2AuthorizationRequestResolver; // 표준 파라미터 사용하기 위해

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository, String authorizationRequestBaseUri) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizationRequestMatcher = PathPatternRequestMatcher.withDefaults().matcher(authorizationRequestBaseUri + "/{registrationId}");
        defaultOAuth2AuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, authorizationRequestBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        String registrationId = this.resolveRegistrationId(request);
        if (registrationId == null) {
            return null;
        }

        if (registrationId.equals("keycloakWithPKCE")) {
            OAuth2AuthorizationRequest oAuth2AuthorizationRequest = defaultOAuth2AuthorizationRequestResolver.resolve(request);
            return customResolve(oAuth2AuthorizationRequest);
        }

        return defaultOAuth2AuthorizationRequestResolver.resolve(request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        String registrationId = this.resolveRegistrationId(request);
        if (registrationId == null) {
            return null;
        }

        if (registrationId.equals("keycloakWithPKCE")) {
            OAuth2AuthorizationRequest oAuth2AuthorizationRequest = defaultOAuth2AuthorizationRequestResolver.resolve(request);
            return customResolve(oAuth2AuthorizationRequest);
        }

        return defaultOAuth2AuthorizationRequestResolver.resolve(request);
    }

    private OAuth2AuthorizationRequest customResolve(OAuth2AuthorizationRequest oAuth2AuthorizationRequest) {
        Map<String, Object> extraParam = new HashMap<>();
        extraParam.put("customName1", "customValue1");
        extraParam.put("customName2", "customValue2");
        extraParam.put("customName3", "customValue3");

        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest
                        .from(oAuth2AuthorizationRequest)
                        .additionalParameters(extraParam);

        DEFAULT_PKCE_APPLIER.accept(builder); // 표준 파라미터는 customResolve 호출전에 포함됨 (client secret) + PKCE 추가 파라미터 (code challenge, code challenge method) 사용

        return builder.build();
    }

    private String resolveRegistrationId(HttpServletRequest request) {
        return this.authorizationRequestMatcher.matches(request) ? (String)this.authorizationRequestMatcher.matcher(request).getVariables().get("registrationId") : null;
    }

}
