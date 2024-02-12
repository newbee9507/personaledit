package com.seb_main_034.SERVER.auth.filter;

import com.seb_main_034.SERVER.auth.jwt.JwtTokenizer;
import com.seb_main_034.SERVER.auth.userdetails.UsersDetailsService;
import com.seb_main_034.SERVER.auth.utils.UsersAuthorityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter { // 이 클래스를 상속받아 request당 한번만 실행되는 필터생성

    private final JwtTokenizer jwtTokenizer;
    private final UsersAuthorityUtils authorityUtils;
    private final UsersDetailsService usersDetailsService;

    private final String bea = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            Map<String, Object> claims = verifyJws(request); // JWT검증 메서드
            log.info("JWT 인증 성공");
            setAuthenticationToContext(claims); // 인증된 객체를 SecurityContext에 저장하는 메서드
            log.info("SecurityContext에 저장 성공");
        } catch (SignatureException se) {
            request.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ee);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        } // 메서드 실행중, 어느 한 곳에서 예외가 터지면 인증객체를 SecurityContext에 저장하지 않음.
        // 이 상태에서 다음 필터들을 진행하다보면 인증정보가 없어서 AuthenticationException이 발생함.
        // 이 예외는 UserAuthenticationEntryPoint 가 받을 예정.

        //서명검증 -> 저장 순으로 모두 성공한다면, 다음 필터를 호출
        filterChain.doFilter(request, response);

    }

    @Override // 특정 조건을 만족하면 이 필터의 동작을 건너뛰고 다음 필터실행.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // 헤더의 값을 얻어냄. 엑세스 토큰 생성시 Authorization이란 이름으로 엑세스토큰 값을 할당했었음
        String authorization = request.getHeader("Authorization");

        //헤더의 값이 존재하지 않거나 Bearer로 시작하지 않으면 필터동작을 하지않음.
        return authorization == null || !authorization.startsWith(bea);
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {

        // request의 헤더에서 jwt를 얻어내고, 엑세스 키를 생성할때 앞에 붙인 Bearer을 제거.
        String jws = request.getHeader("Authorization").replace(bea,"");
        log.info("authorization = {}", jws);
        //서명을 검증하기 위한 비밀키를 얻어냄.
        String encodedBase64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        // 헤더에서 얻어온 jwt와 비밀키로 Claims를 얻어냄. 인증된 유저의 정보(?)
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, encodedBase64SecretKey).getBody();

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {

        // 인증된 유저의 정보에서 email부분을 얻어냄. 생성시 email이란 key로 유저의 이메일을 부여했었음.
        String email = (String) claims.get("email");
        UserDetails userDetails = usersDetailsService.loadUserByUsername(email);

        // 인능된 유저의 정보에서 roles부분을 얻어냄. 생성시 roles이란 key로 권한의 리스트를 부여했었음.
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));

        //둘을 통해 인증된 객체를 생성
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        //SecurityContextHolder에 인증된 객체를 저장 컨트롤러에서 @AuthenticationPrincipal 로 가져올 수 있게됨
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
