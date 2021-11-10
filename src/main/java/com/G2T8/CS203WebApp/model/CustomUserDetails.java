package com.G2T8.CS203WebApp.model;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.*;

public class CustomUserDetails implements UserDetails {
    private User user;

    public CustomUserDetails(final User user) {
        this.user = user;
    }

    public CustomUserDetails() {
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> grantedAuths = new HashSet<>();
        if (user != null) {
            grantedAuths.add(new SimpleGrantedAuthority(user.getRole()));
        }
        return grantedAuths;

        }

    @Override
    public String getPassword() {
        if (this.user == null) {
            return null;
        }
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        if (this.user == null) {
            return null;
        }
        return this.user.getEmail();
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

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "CustomUserDetails [user=" + user + "]";
    }
}
