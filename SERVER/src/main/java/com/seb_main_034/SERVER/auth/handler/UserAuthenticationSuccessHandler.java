package com.seb_main_034.SERVER.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb_main_034.SERVER.auth.dto.LoginSuccessResponseDto;
import com.seb_main_034.SERVER.users.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String isUser = "USER";
    private final String isAdmin = "ADMIN";

    // 인증에 성공했을시.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        Users principal = (Users) authentication.getPrincipal();

        Long userId = principal.getUserId();
        String userEmail = principal.getEmail();
        String Role = isUser;
        if (principal.getRoles().contains("ADMIN")) {
            Role = isAdmin;
        }

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), new LoginSuccessResponseDto(userId ,userEmail, Role));

        log.info("{} 회원 로그인 성공", principal.getNickName());

    }
}
