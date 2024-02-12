package com.seb_main_034.SERVER.auth.userdetails;

import com.seb_main_034.SERVER.exception.ExceptionCode;
import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.users.repository.UserRepository;
import com.seb_main_034.SERVER.auth.utils.UsersAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsersDetailsService implements UserDetailsService {

    private final UserRepository repository;
    private final UsersAuthorityUtils authorityUtils; // 권한부여를 위한 클래스

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // db에서 유저정보를 찾아 스프링 시큐리티에게 인증을 위임(?)
        Users User = repository.findByEmail(email).orElseThrow(
                () -> new GlobalException(ExceptionCode.USER_NOT_FOUND)); // 이 프로젝트는 로그인 시 Id를 이메일로 사용함

        return new UsersDetails(User);
    }

    /**
     * 데이터베이스에서 조회한 회원 정보를 Spring Security의 회원정보로 변환하는 과정과 회원의 권한 정보를 생성하는 과정의 캡슐화
     */
    private final class UsersDetails extends Users implements UserDetails { //

        UsersDetails(Users user) { // db에서 가져온 유저를 시큐리티가 검증하기 위한 객체를 만듬. db에있는 유저정보와 일치해야 함
            setUserId(user.getUserId());
            setEmail(user.getEmail());
            setPassword(user.getPassword());
            setNickName(user.getNickName());
            setProFilePicture(user.getProFilePicture());
            setRoles(user.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() { // Userservice에서 저장할때 이메일로 권한을 설정했음. 그 권한을 가져옴
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}

