package com.practice.jwttokenauth.security;

import com.practice.jwttokenauth.models.user.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;
    private Collection<? extends GrantedAuthority> userAuthorities;

    public static CustomUserDetails build(final User user) {
        final Collection<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        return new CustomUserDetails(user, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getUserAuthorities();
    }

    @Override
    public String getPassword() {
        return this.getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return this.getUser().getEmail();
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
