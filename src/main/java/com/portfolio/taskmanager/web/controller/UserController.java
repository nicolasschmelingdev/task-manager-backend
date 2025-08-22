package com.portfolio.taskmanager.web.controller;

import com.portfolio.taskmanager.application.service.UserService;
import com.portfolio.taskmanager.infrastructure.persistence.entity.UserEntity;
import com.portfolio.taskmanager.web.dto.UserCreateRequest;
import com.portfolio.taskmanager.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreateRequest request,
                                                 UriComponentsBuilder uriBuilder) {
        UserEntity user = userService.register(request.username(), request.password(), request.role());
        URI location = uriBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(new UserResponse(user.getId(), user.getUsername(), user.getRole()));
    }
}
