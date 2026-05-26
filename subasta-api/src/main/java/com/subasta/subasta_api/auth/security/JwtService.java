package com.subasta.subasta_api.auth.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import com.subasta.subasta_api.model.Usuario;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(Usuario usuario) {
        try {
            JWSSigner signer = new MACSigner(secret.getBytes());

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(usuario.getUsername())
                    .claim("role", usuario.getRole().name())
                    .claim("userId", usuario.getId())
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plusSeconds(expiration)))
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            SignedJWT jwt = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims
            );

            jwt.sign(signer);
            return jwt.serialize();
        } catch (Exception e) {
            log.error("Error generando JWT: {}", e.getMessage(), e);
            throw new RuntimeException("Error generado JWT", e);
        }
    }

    public JWTClaimsSet validateToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());

            if (!jwt.verify(verifier)) {
                throw new RuntimeException("Token inválido");
            }

            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            if (claims.getExpirationTime().before(new Date())) {
                throw new RuntimeException("Token expirado");
            }

            return claims;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error validando JWT", e);
        }
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }
}