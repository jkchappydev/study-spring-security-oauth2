package com.studyspringsecurityoauth2.config;

<<<<<<< HEAD
import com.studyspringsecurityoauth2.filter.CustomOAuth2AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class OAuth2ClientConfig {

    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    public OAuth2ClientConfig(DefaultOAuth2AuthorizedClientManager authorizedClientManager, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientManager = authorizedClientManager;
        this.authorizedClientRepository = authorizedClientRepository;
=======
import com.studyspringsecurityoauth2.service.CustomOAuth2UserService;
import com.studyspringsecurityoauth2.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/js/**", "/static/images/**", "/static/css/**","/static/scss/**");
>>>>>>> study/section10
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
<<<<<<< HEAD
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/","/oauth2Login","/v2/oauth2Login","/client").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/client")
                )
                .oauth2Client(Customizer.withDefaults())
                .addFilterBefore(customOAuth2AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
=======
        // 8
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/user")
                            .access(AuthorityAuthorizationManager.hasAnyRole("SCOPE_profile", "SCOPE_email"))
                        .requestMatchers("/api/oidc")
                            .access(AuthorityAuthorizationManager.hasAnyRole("SCOPE_op enid"))
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/").permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)))
                /*.logout(logout -> logout.logoutSuccessUrl("/"))*/
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
>>>>>>> study/section10
        ;

        return http.build();
    }

<<<<<<< HEAD
    private CustomOAuth2AuthenticationFilter customOAuth2AuthenticationFilter() {
        CustomOAuth2AuthenticationFilter customOAuth2AuthenticationFilter =
                new CustomOAuth2AuthenticationFilter(authorizedClientManager, authorizedClientRepository);

        customOAuth2AuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.sendRedirect("/home");
        });

        return customOAuth2AuthenticationFilter;
    }

=======
>>>>>>> study/section10
}
