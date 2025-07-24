package com.studyspringsecurityoauth2.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public HomeController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/home")
    public String home(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
                "keycloak", oAuth2AuthenticationToken.getName());

        model.addAttribute("oAuth2AuthenticationToken", oAuth2AuthenticationToken);
        model.addAttribute("AccessToken", oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        if(oAuth2AuthorizedClient.getRefreshToken() != null) { // client credentials 방식은 refresh token 미발급
            model.addAttribute("RefreshToken", oAuth2AuthorizedClient.getRefreshToken().getTokenValue());
        }

        return "home";
    }

}