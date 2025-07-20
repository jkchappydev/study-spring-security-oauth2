package com.studyspringsecurityoauth2.service;

import com.studyspringsecurityoauth2.model.ProviderUser;
import com.studyspringsecurityoauth2.model.User;
import com.studyspringsecurityoauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 5
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(String registrationId, ProviderUser providerUser) {
        User user = User.builder().registrationId(registrationId)
                .id(providerUser.getId())
                .username(providerUser.getUsername())
                .password(providerUser.getPassword())
                .email(providerUser.getEmail())
                .authorities(providerUser.getAuthorities())
                .build();

        userRepository.register(user);
    }

}
