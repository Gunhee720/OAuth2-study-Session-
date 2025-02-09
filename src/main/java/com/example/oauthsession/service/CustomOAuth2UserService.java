package com.example.oauthsession.service;

import com.example.oauthsession.dto.*;
import com.example.oauthsession.entity.UserEntity;
import com.example.oauthsession.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    //DefaultOAuth2UserService OAuth2UserService의 구현체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("registrationId: " + registrationId);
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
            System.out.println("카카오로 유저정보 받음"+oAuth2Response.getEmail()+oAuth2Response.getName()+oAuth2Response.getProviderId());
        }
        else {

            return null;
        }

        String role = "ROLE_USER";

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existuser = userRepository.findByUsername(username);

        if (existuser == null) {
            UserEntity userEntity = UserEntity.builder()
                            .username(username)
                                    .email(oAuth2Response.getEmail())
                                            .role(role)
                                                    .build();

            userRepository.save(userEntity);
        }
        else {
            existuser.setUsername((username));
            existuser.setEmail(oAuth2Response.getEmail());
            role = existuser.getRole();
            userRepository.save(existuser);
        }
        return new CustomOAuth2User(oAuth2Response, role);


    }
}
