/*
package com.seb_main_034.SERVER.auth.controller;

import com.seb_main_034.SERVER.auth.service.GoogleOAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleOAuth2Controller {

    @Autowired
    private GoogleOAuth2Service googleOAuth2Service;

    @PostMapping("/api/auth/google")
    public String googleLogin(@RequestBody OAuth2AuthenticationToken authentication) {
        googleOAuth2Service.authenticateWithGoogle(authentication);

        // TODO: 성공적인 인증 후에 수행할 로직 (예: 토큰 발급)
        return "Successfully authenticated with Google";
    }
}*/
