package com.reon.order_backend.jwt;

import com.reon.order_backend.document.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expiration-time}")
    private Long expirationTime;

    public String getJwtFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("JWT")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String generateToken(User user) {
        String email = user.getEmail();
        String roles = user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        return Jwts.builder()
                .subject(email)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + expirationTime)))
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}