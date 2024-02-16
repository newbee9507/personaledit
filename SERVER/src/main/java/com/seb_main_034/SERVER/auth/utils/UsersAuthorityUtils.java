package com.seb_main_034.SERVER.auth.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsersAuthorityUtils {

    public UsersAuthorityUtils( @Value("${mail.address.admin}") String adminEmail) {
        this.adminEmail = adminEmail;
    }

    @Getter
    private String adminEmail;

    private final List<String> ADMIN_ROLES = List.of("ADMIN", "USER");
    private final List<String> USER_ROLES = List.of("USER");

    public List<String> createRoles(String email) { // 이메일로 권한을 부여함
        if (email.equals(adminEmail)) {
            return ADMIN_ROLES;
        }
        return USER_ROLES;
    }

    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // 반드시 ROLE_역할 이 형식으로 반환해야함
                .collect(Collectors.toList());
        return authorities;
    }
}
