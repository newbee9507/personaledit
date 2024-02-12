package com.seb_main_034.SERVER.auth.handler;

import com.seb_main_034.SERVER.auth.utils.ErrorResponder;
import com.seb_main_034.SERVER.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        //비밀번호가 틀리거나 || 아이디가 존재하지 않을경우 모두 미인가로 처리해버림. 보안상 아이디가 존재한다는것을 알리지 않는게 더 좋아보임
        //기타 다른 예외들도 존재하겠지만, 우선은 이 두 예외만 존재한다고 가정하겠음.
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            ErrorResponder.sendErrorResponse(response, ExceptionCode.LOGIN_FAIL);
        }

    }
}
