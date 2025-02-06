package com.example.oauthsession.dto;

import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attribute;
    private final String providerId;

    public KakaoResponse(Map<String, Object> attribute) {

        this.attribute = (Map<String, Object>) attribute.get("kakao_account");
        this.providerId = String.valueOf(attribute.get("id")); // 최상위 id 값
    }
    @Override
    public String getProvider() {

        return "kakao";
    }

    @Override
    public String getProviderId() {

        return providerId;
    }

    @Override
    public String getEmail() {

        return attribute.get("email") != null ? attribute.get("email").toString() : null;
    }

    @Override
    public String getName() {
        Map<String, Object> profile = (Map<String, Object>) attribute.get("profile");
        return profile != null && profile.get("nickname") != null ? profile.get("nickname").toString() : null;
    }
}
