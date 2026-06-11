package com.example.shree.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {


    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    private SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
     //generating token
    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 10000))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

//    extracting claims of token
    public Claims extractClaims(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
//   extracting username from claims the
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

//  validating the token username is equal to userdetail.username and expiration of token
    public Boolean validateToken(String username, String token, UserDetails userDetails) {
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

//    checking if token expired returns true if the token expired
    private Boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
