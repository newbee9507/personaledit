package com.seb_main_034.SERVER.forTestUtils;

import com.seb_main_034.SERVER.auth.userdetails.UsersDetailsService;
import com.seb_main_034.SERVER.auth.utils.UsersAuthorityUtils;
import com.seb_main_034.SERVER.users.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<ForMockTestCustomUser> {

    @Autowired
    private UsersAuthorityUtils authorityUtils;

    @Autowired
    private ForTestUserDetailsService detailsService;

    @Override
    public SecurityContext createSecurityContext(ForMockTestCustomUser annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        Long userId = annotation.userId();

        Users user = new Users(annotation.email());
        user.setUserId(userId);
        user.setRoles(createRoles(user.getEmail()));

        UserDetails userDetails = detailsService.loadUserByUsername(user.getEmail());
        Authentication token =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        authorityUtils.createAuthorities(user.getRoles()));

        context.setAuthentication(token);
        return context;
    }

    private List<String> createRoles(String email) {
        if(email.equals("admin@gmail.com")) return List.of("ADMIN", "USER");
        return List.of("USER");
    }
}
