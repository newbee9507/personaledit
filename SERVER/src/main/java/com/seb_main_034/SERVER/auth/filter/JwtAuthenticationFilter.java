package com.seb_main_034.SERVER.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb_main_034.SERVER.auth.jwt.JwtTokenizer;
import com.seb_main_034.SERVER.auth.dto.LoginDto;
import com.seb_main_034.SERVER.auth.service.RefreTokenService;
import com.seb_main_034.SERVER.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; // UserDetailsService에게 클라이언트가 보낸 토큰으로 검색요청(?) 보낼거라 예상
    private final JwtTokenizer jwtTokenizer;
    private final RefreTokenService refreTokenService;

    private final String bea = "Bearer ";

    @SneakyThrows // 발생할 수 있는 예외를 명시하지 않아도 되도록 해줌. 에러발생시 런타임 예외로 래핑해줌 단순히 코드를 간결하게 해주는 용도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        // request에서 들어온 이메일, 비밀번호를 LoginDto로 역직렬화하는 코드 2줄.
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        //LoginDto에서 클라이언트가 보내온 이메일, 비밀번호를 읽어내, 인증요청용 토큰을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // authenticationManager에게 토큰을 주면서 인증해달라 요청.
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override // 인증 성공시 호출
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {

        // 인증에 성공하면 인증된 Authentication 객체의 principal 필드에 Users 객체가 할당됨.
        Users user = (Users) authResult.getPrincipal();

        String accessToken = delegateAccessToken(user);
        String refreshToken = delegateRefreshToken(user);

        LocalDateTime expirationTime = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);

        //response 헤더에 엑세스 토큰을 담아 보냄. 이후 클라이언트가 request 헤더에 추가해서 자격증명에 사용
        response.setHeader("Authorization", bea + accessToken);
        response.setHeader("Refresh", bea + refreshToken); // 필수적이지 않음. 제외할 수 있음.
        response.setHeader("expirationTime", String.valueOf(expirationTime));
        refreTokenService.saveToken(refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    // 액세스 토큰을 생성하는데 필요한 정보들을 세팅해 JwtTokenizer 클래스의 생성 메서드로 전달. 만들어진 토큰을 여기서 반환함
    private String delegateAccessToken(Users user) {
        Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenLifeTime(jwtTokenizer.getAccessTokenLifeTime());
        String encodedBase64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.createAccessToken(claims, subject, expiration, encodedBase64SecretKey);

        return accessToken;
    }

    // 리프레쉬 토큰을 생성하는데 필요한 정보들을 세팅해 JwtTokenizer 클래스의 생성 메서드로 전달. 만들어진 토큰을 여기서 반환함
    private String delegateRefreshToken(Users user) {
        Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenLifeTime(jwtTokenizer.getRefreshTokenLifeTime());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.createRefreshToken(claims, subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}
