package com.adrian.Habits.jwt.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.adrian.Habits.jwt.details.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

// Util class to generate JWT token
@Component
@AllArgsConstructor
public class JwtUtil {
    private final JwtConfiguration jwtConfiguration;

    private String getSecret() {
        return jwtConfiguration.getSecret();
    }

    @PostConstruct
public void init() {
    System.out.println(">>> JWT Secret loaded: " + getSecret());
}

    // Creates and builds the token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
            .signWith(SignatureAlgorithm.HS256, getSecret()) // hs256 = hash based authentication code
            .compact();
    }

    // Returns the token claims by parsing via secret key
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .setSigningKey(getSecret())
            .parseClaimsJws(token)
            .getBody();
    }

    // public method to generate the token
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId()); // Optional
        
        return createToken(claims, userDetails.getUsername());
    }

    // Extract claims for each type
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Checks if the token is still valid
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
