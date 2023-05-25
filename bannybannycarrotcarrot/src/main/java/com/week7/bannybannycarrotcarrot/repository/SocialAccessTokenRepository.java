package com.week7.bannybannycarrotcarrot.repository;

import com.week7.bannybannycarrotcarrot.entity.SocialAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccessTokenRepository extends JpaRepository<SocialAccessToken, Long> {

    Optional<SocialAccessToken> findByAccountEmail(String email);

}
