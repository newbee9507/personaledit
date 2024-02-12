//package com.seb_main_034.SERVER.auth.handler;
//
//import com.seb_main_034.SERVER.auth.jwt.JwtTokenizer;
//import com.seb_main_034.SERVER.auth.utils.UsersAuthorityUtils;
//import com.seb_main_034.SERVER.users.entity.Users;
//import com.seb_main_034.SERVER.users.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.URI;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RequiredArgsConstructor
//public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler { // 이 클래스 상속시, 리다이렉트를 쉽게 하는 sendRedirect()사용 가능
//
//    private final JwtTokenizer jwtTokenizer;
//    private final UsersAuthorityUtils authorityUtils;
//    private final UserService service;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//        var oAuth2User = (OAuth2User)authentication.getPrincipal();
//        String email = String.valueOf(oAuth2User.getAttributes().get("email")); // Resource Owner의 이메일주소
//        List<String> authorities = authorityUtils.createRoles(email); // 우리 서비스의 Resource(영화)등과 연관관계를 맺기 위해.
//
//        saveUser(email);
//        redirect(request, response, email, authorities);
//    }
//
//    private void saveUser(String email) {
//        Users user = new Users(email);
//        service.save(user);
//    }
//
//    // 프론트엔드에 전달하기 위한 메서드
//    private void redirect(HttpServletRequest request,
//                          HttpServletResponse response,
//                          String email, List<String> authorities) throws IOException {
//
//        String accessToken = delegateAccessToken(email, authorities);
//        String refreshToken = delegateRefreshToken(email);
//
//        String uri = createURI(accessToken, refreshToken).toString();
//        getRedirectStrategy().sendRedirect(request, response, uri);   // 프론트엔드로 리다이렉트
//    }
//
//    private String delegateAccessToken(String username, List<String> authorities) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", username);
//        claims.put("roles", authorities);
//
//        String subject = username;
//        Date expiration = jwtTokenizer.getTokenLifeTime(jwtTokenizer.getAccessTokenLifeTime());
//
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        String accessToken = jwtTokenizer.createAccessToken(claims, subject, expiration, base64EncodedSecretKey);
//
//        return accessToken;
//    }
//
//    private String delegateRefreshToken(String username) {
//        String subject = username;
//        Date expiration = jwtTokenizer.getTokenLifeTime(jwtTokenizer.getRefreshTokenLifeTime());
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        String refreshToken = jwtTokenizer.createRefreshToken(subject, expiration, base64EncodedSecretKey);
//
//        return refreshToken;
//    }
//
//    //프론트엔드 애플리케이션 쪽의 URL을 생성
//    private URI createURI(String accessToken, String refreshToken) {
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("access_token", accessToken);
//        queryParams.add("refresh_token", refreshToken);
//
//        return UriComponentsBuilder
//                .newInstance()
//                .scheme("http")
//                .host("034pro.s3-website.ap-northeast-2.amazonaws.com") // 로컬테스트시 localhost로 바꿀것
////                .port(80)
//                .path("/")
//                .queryParams(queryParams)
//                .build()
//                .toUri();
//    }
//
//}
