package com.seb_main_034.SERVER.auth.service;

import com.seb_main_034.SERVER.auth.jwt.RefreToken;
import com.seb_main_034.SERVER.auth.jwt.RefreTokenRepository;
import com.seb_main_034.SERVER.exception.ExceptionCode;
import com.seb_main_034.SERVER.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreTokenService {

    private final RefreTokenRepository repository;

    public void saveToken(String refreshToken) {
        RefreToken token = new RefreToken(refreshToken);
        repository.save(token);
    }

//    public void deleteRefreshToken(String refreshToken) { // 리프레쉬 토큰은 약 2주를 유지시킬 예정
//        if (repository.findByToken(refreshToken).isPresent()) {
//            repository.delete(repository.findByToken(refreshToken).get());
//        }
//    }

    public RefreToken findByToken(String refreshToken) {
        RefreToken token = repository.findByToken(refreshToken).orElseThrow(() -> new GlobalException(ExceptionCode.UN_AUTHORITY));
        Duration duration = Duration.between(token.getCreateAt(), LocalDateTime.now());
        if (duration.toDays() > 14) {
            repository.delete(token);
            throw new GlobalException(ExceptionCode.UN_AUTHORITY);
        }
        return token;
    }
}
