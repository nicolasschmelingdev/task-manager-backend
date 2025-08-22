package com.portfolio.taskmanager.security;

import com.portfolio.taskmanager.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties props;
    private final Key key;

    public JwtTokenProvider(JwtProperties props) {
        this.props = props;
        byte[] keyBytes;
        String secret = props.getSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret is not configured (jwt.secret)");
        }
        // Accept base64 secrets; if not base64, derive a 256-bit key from the text using SHA-256
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (RuntimeException e) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to derive JWT key", ex);
            }
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(props.getExpirationMillis());
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
