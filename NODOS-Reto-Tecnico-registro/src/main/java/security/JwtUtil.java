package security;

import io.jsonwebtoken.*;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtUtil {
    private final String SECRET="secretkeysecretkeysecretkey";
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


