package com.adrian.Habits.jwt.details;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.adrian.Habits.model.UserEntity;

// UserDetails class
public class CustomUserDetails implements UserDetails{
    private final UserEntity userEntity;
    
    public CustomUserDetails(UserEntity userEntity){
        this.userEntity = userEntity;
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    // No roles for now, "?" is a wildcard indicator
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Long getId() {
        return userEntity.getId();
    }

    public UserEntity getUser() {
        return userEntity;
    }
}
