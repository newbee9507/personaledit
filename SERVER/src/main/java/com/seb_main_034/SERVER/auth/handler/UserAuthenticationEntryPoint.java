package com.seb_main_034.SERVER.auth.handler;

import com.seb_main_034.SERVER.auth.utils.ErrorResponder;
import com.seb_main_034.SERVER.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 검증필터에서 발생한 예외를 여기서 받음. 저장할 때 exception으로 저장했었음.
        Exception exception = (Exception) request.getAttribute("exception");
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            ErrorResponder.sendErrorResponse(response, ExceptionCode.LOGIN_FAIL);
        }
        ErrorResponder.sendErrorResponse(response, ExceptionCode.UN_AUTHORITY);
    }

    private void logExceptionMessage(AuthenticationException authException, Exception exception) {
        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.warn("인증이 실패했습니다. -> {}", message);
    }
}
