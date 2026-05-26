package com.subasta.subasta_api.auth.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String username,
    String role
) {}