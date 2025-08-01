package com.studyspringsecurityoauth2.controller;

<<<<<<< HEAD
import org.springframework.stereotype.Controller;
=======
import com.studyspringsecurityoauth2.common.util.OAuth2Utils;
import com.studyspringsecurityoauth2.model.PrincipalUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
>>>>>>> study/section10
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
<<<<<<< HEAD

    @GetMapping("/")
    public String index(){
        return "index";
    }

}
=======
    // 7
    // 인증 받은 사용자 접근 가능
    @GetMapping("/")
    public String index(Model model, Authentication authentication, @AuthenticationPrincipal PrincipalUser principalUser) { // 이제 OAuth2User 가 아니라 PrincipalUser 이다.
        // 인증객체를 구현체 타입으로 변환.
        // OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        /*if (oAuth2AuthenticationToken != null) {
            Map<String, Object> attributes = oauth2User.getAttributes();
            String name = (String) attributes.get("name");

            // 네이버 별도 처리
            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("naver")) {
                Map<String, Object> response = (Map) attributes.get("response");
                name = (String) response.get("name");
            }
            model.addAttribute("user", name);
        }*/

        if (principalUser != null) {
            String userName =  principalUser.providerUser().getUsername();

            // 분기 할 필요 없음
            // 이미 PrincipalUser 에서 (implements UserDetails, OAuth2User, OidcUser) 구분되어서 처리하게 해놓았음
            /*if (authentication instanceof OAuth2AuthenticationToken) { // OAuth2User 또는 OidcUser 타입
                userName = OAuth2Utils.oAuth2UserName((OAuth2AuthenticationToken) authentication, principalUser);
            } else { // UserDetails 타입
                userName = principalUser.providerUser().getUsername();
            }*/

            model.addAttribute("user", userName);
            model.addAttribute("provider", principalUser.providerUser().getProvider());
        }

        return "index";
    }

}
>>>>>>> study/section10
