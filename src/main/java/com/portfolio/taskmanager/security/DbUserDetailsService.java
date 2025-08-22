package com.portfolio.taskmanager.security;

import com.portfolio.taskmanager.infrastructure.persistence.entity.UserEntity;
import com.portfolio.taskmanager.infrastructure.persistence.repository.UserSpringRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserSpringRepository repository;

    public DbUserDetailsService(UserSpringRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity u = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(u.getUsername(), u.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole())));
    }
}
