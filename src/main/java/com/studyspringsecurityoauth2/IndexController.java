package com.studyspringsecurityoauth2;

<<<<<<< HEAD
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
=======
>>>>>>> study/section8
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

<<<<<<< HEAD
    private final ClientRegistrationRepository clientRegistrationRepository;

    public IndexController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/")
    public String index() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        String clientId = clientRegistration.getClientId();
        System.out.println("clientId = " + clientId);

        String redirectUri = clientRegistration.getRedirectUri();
        System.out.println("redirectUri = " + redirectUri);

        return "index";
    }

}
=======
    @GetMapping("/")
    public String index() {
        return "index";
    }

}
>>>>>>> study/section8
