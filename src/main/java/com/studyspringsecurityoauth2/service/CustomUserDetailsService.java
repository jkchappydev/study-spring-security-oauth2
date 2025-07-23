package com.studyspringsecurityoauth2.service;

import com.studyspringsecurityoauth2.common.converters.ProviderUserConverter;
import com.studyspringsecurityoauth2.common.converters.ProviderUserRequest;
import com.studyspringsecurityoauth2.model.PrincipalUser;
import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.model.users.User;
import com.studyspringsecurityoauth2.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserService userService, UserRepository userRepository, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter, UserRepository userRepository1) {
        super(userService, userRepository, providerUserConverter);
        this.userRepository = userRepository1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            user = User.builder()
                    .id("1")
                    .username("user1")
                    .password("{noop}1234")
                    .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                    .email("user@a.com")
                    .build();
        }

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(user);
        ProviderUser providerUser = providerUser(providerUserRequest);

        // CustomOAuth2UserService 에서 반환하는 OAuth2User 타입과 여기서 반환하는 UserDetails 타입을 일치 시켜야 함
        // 따라서, 해당 타입을 함께 사용할 수 있는 PrincipalUser 타입을 만든다.
        return new PrincipalUser(providerUser);
    }

}
