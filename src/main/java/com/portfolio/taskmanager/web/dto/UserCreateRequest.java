package com.portfolio.taskmanager.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
        @NotBlank String username,
        @NotBlank String password,
        String role
) {}
