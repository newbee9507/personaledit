package com.seb_main_034.SERVER.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenizer {

    @Getter
    @Value("${jwt.key}")
    private String secretKey; // JWT 생성 및 검증에 사용되는 비밀키 정보.

    @Getter
    @Value("${jwt.access-token-life-time}")
    private int accessTokenLifeTime;  // accessToken 만료시간 정보

    @Getter
    @Value("${jwt.refresh-token-life-time}")
    private int refreshTokenLifeTime; // refreshToken 만료시간 정보

    // 비밀키를 암호화해 생성
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Map<String, Object> claims,
                                    String subject,
                                    Date expiration,
                                    String base64SecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64SecretKey); // base64로 인코딩된 문자열로 Key객체 생성

        return Jwts.builder()
                .setClaims(claims)  // 주로 인증된 사용자와 관련된 정보
                .setSubject(subject) // JWT에 대한 제목?
                .setIssuedAt(Calendar.getInstance().getTime()) // 발행일자
                .setExpiration(expiration) // 만료일시
                .signWith(key) // 서명을 위한 Key. 위에서 생성한 Key 객체
                .compact(); // JWT 생성 및 직렬화
    }

    public String createRefreshToken(Map<String, Object> claims, String subject, Date expiration, String base64SecretKey) {
        // Access 토큰 만료시, 재생성해주는 Refresh 토큰 생성 메서드
        // Access 토큰을 새로 발급해주는 역할이기에 사용자와 관련된 정보는 추가하지 않아도 됨
        Key key = getKeyFromBase64EncodedKey(base64SecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    // 유저정보들을 얻어냄(?) 성공했으면, 서명검증에 성공했다는 의미
    public Jws<Claims> getClaims(String jws, String base64SecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64SecretKey);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key) // 이 부분에서 옳은 키를 넣었다면 서명검증에 성공
                .build()
                .parseClaimsJws(jws);
        return claims;
    }

    // JWT 검증 메서드. JWT에 포함된 Signature를 검증함으로써 JWT의 위/변조 여부를 확인
    // 서명시 사용된 비밀키를 이용해 내부적으로 검증. 성공하면 Claims를 얻음
    public void verifySignature(String jws, String base64SecretKey) { // jws = Signature를 포함한 JWT
        Key key = getKeyFromBase64EncodedKey(base64SecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key) // 서명에 사용된 비밀키를 설정
                .build()
                .parseClaimsJws(jws); // JWT를 파싱해서 Claims를 획득.
    }

    public Date getTokenLifeTime(int lifeTime) { // JWT 만료시간을 지정하는 메서드
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, lifeTime);
        Date time = calendar.getTime();

        return time;
    }

    private Key getKeyFromBase64EncodedKey(String base64SecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64SecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes); // 윗줄에서 생성한 배열을 기반으로 적절한 HMAC 알고리즘을 찾아서 적용한 Key객체 생성.

        return key;
    }
}
