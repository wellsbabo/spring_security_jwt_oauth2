package com.example.securityTest.common.oauth2.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    /**
     * 서비스에 사용하고 싶은 유저 정보들을 가져오는 메소드를 생성
     * 각 소셜에서 제공하는 정보 중에 사용하고 싶은 정보가 있다면 더 추가해서 사용하면 됨
     *
     */
    public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"
    public abstract String getNickname();
    public abstract String getImageUrl();
}
