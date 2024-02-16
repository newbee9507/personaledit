package com.seb_main_034.SERVER.forTestUtils;

import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.users.entity.Users;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ForTestUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email.equals("admin@gmail.com")) {
            return new ForTestDetails(new Users(1L, email, "adminPw123!", "admin", "adminPicture",
                    List.of("ADMIN", "USER"), new ArrayList<>(), new ArrayList<>()));
        }

        return new ForTestDetails(new Users(2L, "user@gmail.com", "userPw123!", "user", "userPicture",
                List.of("USER"), new ArrayList<>(), new ArrayList<>()));
    }

    private final class ForTestDetails extends Users implements UserDetails {
        ForTestDetails(Users users) {
            setUserId(users.getUserId());
            setEmail(users.getEmail());
            setPassword(users.getPassword());
            setNickName(users.getNickName());
            setProFilePicture(users.getProFilePicture());
            setRoles(users.getRoles());
            setCommentList(users.getCommentList());
            setRecommendedMovies(users.getRecommendedMovies());
        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }

}
