package com.portfolio.taskmanager.application.service;

import com.portfolio.taskmanager.infrastructure.persistence.entity.UserEntity;
import com.portfolio.taskmanager.infrastructure.persistence.repository.UserSpringRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserSpringRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserSpringRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserEntity register(String username, String rawPassword, String role) {
        if (repository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role != null && !role.isBlank() ? role : "USER");
        return repository.save(user);
    }
}
