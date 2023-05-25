package com.week7.bannybannycarrotcarrot.repository;

import com.week7.bannybannycarrotcarrot.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByAccountEmail(String email);

}
