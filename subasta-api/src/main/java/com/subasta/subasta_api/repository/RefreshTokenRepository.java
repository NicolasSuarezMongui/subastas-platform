package com.subasta.subasta_api.repository;

import com.subasta.subasta_api.model.RefreshToken;
import com.subasta.subasta_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.usuario = :usuario")
    void deleteByUsuario(Usuario usuario);
}