package com.rsh.probe.chat.service;

import com.rsh.probe.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
/**
 * minimal implementation of a UserDetailService interface for username, password authentication
 */
public class DBBasedUserDetailsService implements UserDetailsService {
    public static String USER_ROLE = "USER_ROLE";
    public static String ADMIN_ROLE = "ADMIN_ROLE";
    UserService userService;

    @Autowired
    public DBBasedUserDetailsService(UserService userService) {
        super();
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.byUsername(username);
        return user
                .map(_UserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username or Password not found"));

    }
    // TODO this is the least possible implementation
    //      roles and other account information should be persisted
    private static class _UserDetails implements UserDetails {
        User user;
        public _UserDetails(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(USER_ROLE));
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            //return UserDetails.super.isAccountNonExpired();
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            //return UserDetails.super.isAccountNonLocked();
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            //return UserDetails.super.isCredentialsNonExpired();
            return true;
        }

        @Override
        public boolean isEnabled() {
            //return UserDetails.super.isEnabled();
            return true;
        }
    }
}
