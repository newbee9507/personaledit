/*
package com.seb_main_034.SERVER.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2Service {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    // Google OAuth2.0 인증을 처리하는 메서드
    public void authenticateWithGoogle(OAuth2AuthenticationToken authentication) {
        // OAuth2AuthorizedClient 객체를 통해 accessToken과 기타 정보를 얻을 수 있음
        // 예: OAuth2AuthorizedClient authorizedClient = this.authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        // TODO: Google OAuth2.0 인증 로직을 여기에 작성
    }

    // 토큰을 검증하는 메서드
    public boolean verifyToken(String accessToken) {
        // TODO: 토큰 검증 로직을 여기에 작성 (jjwt 라이브러리 활용 가능)
        return true;
    }
}
*/
