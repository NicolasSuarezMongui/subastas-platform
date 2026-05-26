package com.subasta.subasta_api.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.subasta.subasta_api.auth.dto.AuthResponse;
import com.subasta.subasta_api.auth.dto.LoginRequest;
import com.subasta.subasta_api.auth.dto.RegisterRequest;
import com.subasta.subasta_api.auth.security.JwtService;
import com.subasta.subasta_api.model.RefreshToken;
import com.subasta.subasta_api.model.Usuario;
import com.subasta.subasta_api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username ya existe");
        }
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email ya registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRole(Usuario.Role.ROLE_USER);

        usuarioRepository.save(usuario);

        String accessToken = jwtService.generateToken(usuario);
        RefreshToken refreshToken = refreshTokenService.crear(usuario);

        return new AuthResponse(accessToken, refreshToken.getToken(), usuario.getUsername(), usuario.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String accessToken = jwtService.generateToken(usuario);
        RefreshToken refreshToken = refreshTokenService.crear(usuario);

        return new AuthResponse(
            accessToken,
            refreshToken.getToken(),
            usuario.getUsername(),
            usuario.getRole().name()
        );
    }

    public AuthResponse refresh(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.validar(refreshTokenStr);
        Usuario usuario = usuarioRepository.findById(refreshToken.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newAccessToken = jwtService.generateToken(usuario);
        RefreshToken newRefreshToken = refreshTokenService.renovar(refreshToken);

        return new AuthResponse(
            newAccessToken,
            newRefreshToken.getToken(),
            usuario.getUsername(),
            usuario.getRole().name()
        );
    }

    public void logout(String refreshTokenStr) {
        refreshTokenService.revocar(refreshTokenStr);
    }

}