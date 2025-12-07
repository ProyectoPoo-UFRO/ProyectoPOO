package com.example.RESTAPIDB.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final SecretKey secretKey;

    public JWTService(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String nombre) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(nombre)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String extraerNombre(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extraerTodasClaims(token);
        T value = claimResolver.apply(claims) ;
        return value;
    }

    private Claims extraerTodasClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build().parseSignedClaims(token)
                .getPayload();
        return claims;
    }

    public boolean intentarValidarToken(String token, UserDetails userDetails){
        try {
            return validarToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarToken(String token, UserDetails userDetails) {
        String username = extraerNombre(token);
        return username.equals(userDetails.getUsername()) && !estaTokenExpirado(token);
    }


    private boolean estaTokenExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

    private Date extraerFechaExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

}