package com.nodo.retotecnico.security;

import io.jsonwebtoken.*;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private String SECRET;
    
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.SECRET = secret;
    }
    
    private Key getSingKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
    
    public String createToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(getSingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSingKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
