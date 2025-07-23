package com.studyspringsecurityoauth2.converters;

import com.studyspringsecurityoauth2.model.ProviderUser;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.*;

@Component
public class DelegatingProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    private List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> converters;

    // ??????????????????????????
    public DelegatingProviderUserConverter() {
        List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> providerUserConverters =
                Arrays.asList(
                        new OAuth2GoogleProviderUserConverter(),
                        new OAuth2NaverProviderUserConverter()
                );

        this.converters = Collections.unmodifiableList(new LinkedList<>(providerUserConverters));
    }

    // 구글, 네이버, 카카오 인증인지 여부 판단
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        Assert.notNull(providerUserRequest, "providerUserRequest must not be null");

        for (ProviderUserConverter<ProviderUserRequest, ProviderUser> converter : converters) {
            ProviderUser providerUser = converter.convert(providerUserRequest);
            if(providerUser != null) {
                return providerUser;
            }
        }

        return null;
    }

}
