package com.portfolio.taskmanager.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;

public class BasicUserDetailsService implements UserDetailsService {

    
    private final Map<String, String> users = Map.of(
        "admin", "$2a$10$Q1b6zKjJz4F1y3t1a9mC7eN9b9mC7eN9b9mC7eN9b9mC7eN9b9mC7" 
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String hash = users.get(username);
        if (hash == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(username, hash, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
