package com.MindMate.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Claims;
//
//@Component
//public class JwtUtil {
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    private final long expiration = 1000 * 60 * 60;
//
//    public String generateToken(UserDetails userDetails){
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(SignatureAlgorithm.HS256, key)
//                .compact();
//    }
//
//    public String extractUsername(String token){
//        return Jwts.parser()
//                .setSigningKey(key)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token , UserDetails userDetails){
//        String username = extractUsername(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token){
//        Date expiration = Jwts.parser()
//                .setSigningKey(key)
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();
//
//        return expiration.before(new Date());
//    }
//}


@Component
public class JwtUtil {

    // ✅ Hardcoded stable secret (at least 32 chars)
    private final String SECRET = "my-super-secret-key-my-super-secret-key";

    private final long expiration = 1000 * 60 * 60; // 1 hour

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ✅ Generate Token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract Username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ Validate Token
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ✅ Check Expiry
    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // ✅ Extract All Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}