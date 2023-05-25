package com.week7.bannybannycarrotcarrot.security.jwt;

import com.week7.bannybannycarrotcarrot.dto.TokenDto;
import com.week7.bannybannycarrotcarrot.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String AUTHORITY_KEY = "auth";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 60분

    private final Key key;

    private static final long ACCESS_TIME = 1000L * 60 * 60 * 24;
    private static final long REFRESH_TIME = 1000L * 60 * 60 * 24 * 7;
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractToken(String bearerToken) {

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtUtil.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public String generateToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        //access Token 만료 시간 = 현재 시간 + 제한 시간
        Date accessTokenExpiresIn = new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        //Access Token 생성

        return TOKEN_PREFIX +
                Jwts.builder()
                        .setSubject(authentication.getName())
                        .claim(AUTHORITY_KEY, authorities)
                        .setExpiration(accessTokenExpiresIn)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact();
    }

    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITY_KEY) == null) throw new InvalidCookieException("로그인 정보가 잘못되었습니다. 다시 로그인해주세요.");

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITY_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error(e + ": 잘못된 JWT 서명입니다.");
            throw new InvalidCookieException("잘못된 JWT 서명입니다. 다시 로그인해주세요.");
        } catch (ExpiredJwtException e) {
            log.error(e + ": 만료된 JWT 토큰입니다.");
            throw new InvalidCookieException("만료된 JWT 토큰입니다. 다시 로그인해주세요.");
        } catch (UnsupportedJwtException e) {
            log.error(e + ": 지원되지 않는 JWT 토큰입니다.");
            throw new InvalidCookieException("지원되지 않는 JWT 토큰입니다. 다시 로그인해주세요.");
        } catch (IllegalArgumentException e) {
            log.error(e + ": JWT 토큰이 잘못되었습니다.");
            throw new InvalidCookieException("JWT 토큰이 유효하지 않습니다. 다시 로그인해주세요.");
        }
    }

    private Claims parseClaims(String accessToken) {

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public TokenDto createAllToken(String email) {
        return new TokenDto(createToken(email, "Access"), createToken(email, "Refresh"));
    }


    public String createToken(String email, String type) {
        Date date = new Date();

        Long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

    }

    public String createToken(Long id)  {
        //access Token 만료 시간 = 현재 시간 + 제한 시간
        Date accessTokenExpiresIn = new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME);

          return TOKEN_PREFIX +
            Jwts.builder()
                    .setSubject(String.valueOf(id))
                    .claim(AUTHORITY_KEY, UserRole.USER)
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
}
}
