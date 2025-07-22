package com.studyspringsecurityoauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

// 9
public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    private static final String DEFAULT_PREFIX = "ROLE_";
    private static final String SCOPE_PREFIX = "SCOPE_";

    // SimpleAuthorityMapper 그대로 복사
    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet(authorities.size());

        for(GrantedAuthority authority : authorities) {
            mapped.add(this.mapAuthority(authority.getAuthority()));
        }

        // 해당 부분은 필요없
        /*if (this.defaultAuthority != null) {
            mapped.add(this.defaultAuthority);
        }*/

        return mapped;
    }

    // google scope 타입에 맞게 잘라야함
    // 예: "SCOPE_https://www.googleapis.com/auth/userinfo.email"
    private GrantedAuthority mapAuthority(String name) {
        // 가장 마지막 점(.) 이후의 토큰 추출
        if (name.contains(".")) {
            name = SCOPE_PREFIX + name.substring(name.lastIndexOf('.') + 1); // SCOPE_email
        }

        if(DEFAULT_PREFIX.length() > 0 && !name.startsWith(DEFAULT_PREFIX)) {
            name = DEFAULT_PREFIX + name; // ROLE_SCOPE_email
        }

        return new SimpleGrantedAuthority(name);
    }

}
