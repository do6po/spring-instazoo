package com.example.demo.security;

import com.example.demo.entities.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    public Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

    @Autowired
    private Environment env;

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + 300_000);

        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, env.getProperty("app.jwt.secret"))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey("Secret")
                    .parseClaimsJws(token);

            return true;
        } catch (SignatureException
                | MalformedJwtException
                | ExpiredJwtException
                | UnsupportedJwtException
                | IllegalArgumentException e) {
            logger.error(e.getMessage());

            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("Secret")
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getId());
    }
}
