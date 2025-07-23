package com.studyspringsecurityoauth2.converters.enums;

public class OAuth2Config {

    public enum SoclaiType {
        GOOGLE("google"),
        NAVER("naver"),
        KAKAO("kakao");

        private final String socialName;
        SoclaiType(String socialName) {
            this.socialName = socialName;
        }

        public String getSocialName() {
            return socialName;
        }
    }

}
