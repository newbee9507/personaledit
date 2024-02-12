package com.seb_main_034.SERVER.auth.controller;

import com.seb_main_034.SERVER.auth.jwt.JwtTokenizer;
import com.seb_main_034.SERVER.auth.service.RefreTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh")
public class RefreTokenController {

    private final JwtTokenizer jwtTokenizer;
    private final RefreTokenService service;

    private final String bea = "Bearer ";

    @PostMapping
    public String createNewToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = request.getHeader("Refresh").replace(bea,""); // 리프레쉬 토큰 앞의 Bearer 제거

        if (service.findByToken(refreshToken) != null) { // db에 리프레시 토큰이 저장되어 있고, 만료되지 않았다면
            String encodedBase64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            String newAccessToken = createNewAccessToken(refreshToken, encodedBase64SecretKey);

            LocalDateTime expirationTime = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);

            response.setHeader("Authorization", bea + newAccessToken); // jwtTokenizer 클래스를 통해 새로운 엑세스토큰 생성 및 reponse헤더에 설정
            response.setHeader("expirationTime", String.valueOf(expirationTime)); // 만료시간
            return "ok";
        }
        return "no";
    }

    private String createNewAccessToken(String refreshToken, String encodedBase64SecretKey) {
        Map<String, Object> claims = jwtTokenizer.getClaims(refreshToken, encodedBase64SecretKey).getBody();
        String email = (String) claims.get("email");
        Date tokenLifeTime = jwtTokenizer.getTokenLifeTime(jwtTokenizer.getAccessTokenLifeTime());

        return jwtTokenizer.createAccessToken(claims, email, tokenLifeTime, encodedBase64SecretKey);
    }
}
