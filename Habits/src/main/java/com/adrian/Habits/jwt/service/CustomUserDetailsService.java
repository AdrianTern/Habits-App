package com.adrian.Habits.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.adrian.Habits.jwt.details.CustomUserDetails;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.Constants;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(Constants.EXCEPTION_USER_NOT_FOUND));

        // Wrap UserEntity in CustomUserDetails (method from UserDetails)
        return new CustomUserDetails(user);
    }
}
