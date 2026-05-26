package com.subasta.subasta_api.auth.dto;

public record LoginRequest(
    String username,
    String password
) {
}