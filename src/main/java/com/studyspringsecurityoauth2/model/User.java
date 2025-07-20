package com.studyspringsecurityoauth2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

// 3
@Data
@Builder
public class User {

    private String registrationId;
    private String id;
    private String username;
    private String password;
    private String email;
    private String provider;
    private List<? extends GrantedAuthority> authorities;

}
