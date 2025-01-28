package org.youcode.mediconseil.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
@Service
public class JwtService {
    private static final String SECRET = "aa87e9e6f0b3681d13fede8d1db3ecc82f66d6b7de1716cc97aa6d1c0326aa8f9f6f70b07bb399d41878d6d846c0d6915efbf6f0f56ef140b160cfc6554d23806a8c21c9aa150864fc5aab46da1d7967d22eae3cd776438613f5bfc93869c2c400b917e81c3c945a8bbbb478c1085bee79683c4a9d81e6f73e599e57d5a82666d9477c213c0f4851bc8c2126ea5f62d011950d1578b96f7f5848ba9db6fe97181be5ff778b4571c92309aff8e0b83a0cb7bdcf3ba88c1657c835327b0e1bff3650b960b1bbba06403ceed2e9a75e6a6bb75b1266cfe68b9f84c660018a3467980ff0cde85506b55c8edca9e98ed5ca3784067784ca770d9fb5eed428bcc9573d";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("id", String.class)));
    }
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String generateToken(
            Map<String, Object> claims,
            UserDetails userDetails
    ) {
        UUID userId = ((User) userDetails).getId();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("id", userId.toString())
                .claim("role", userDetails.getAuthorities().stream().findFirst().map(Object::toString).orElse(""))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}