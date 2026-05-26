package com.subasta.subasta_api.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.subasta.subasta_api.model.RefreshToken;
import com.subasta.subasta_api.model.Usuario;
import com.subasta.subasta_api.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private static final int REFRESH_TOKEN_DAYS = 7;

    @Transactional
    public RefreshToken crear(Usuario usuario) {
        // Eliminar tokens anteriores del usuario
        refreshTokenRepository.deleteByUsuario(usuario);

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(usuario);
        token.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_DAYS));

        return refreshTokenRepository.save(token);
    }

    public RefreshToken validar(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token no encontrado"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expirado - inicia sesión nuevamente");
        }

        return refreshToken;
    }

    @Transactional
    public RefreshToken renovar(RefreshToken tokenAnterior) {
        Usuario usuario = tokenAnterior.getUsuario();
        refreshTokenRepository.delete(tokenAnterior);

        RefreshToken nuevoToken = new RefreshToken();
        nuevoToken.setToken(UUID.randomUUID().toString());
        nuevoToken.setUsuario(usuario);
        nuevoToken.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_DAYS));

        return refreshTokenRepository.save(nuevoToken);
    }

    @Transactional
    public void revocar(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }

}