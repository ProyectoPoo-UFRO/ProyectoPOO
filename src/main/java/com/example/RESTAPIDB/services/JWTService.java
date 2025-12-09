package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.model.UsuarioDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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

    public String generarToken(UsuarioDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(secretKey)
                .compact();
    }

    public String extraerIdUsuario(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extraerTodasClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extraerTodasClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean intentarValidarToken(String token, UsuarioDetails userDetails){
        try {
            return validarToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarToken(String token, UsuarioDetails userDetails) {
        String userId = extraerIdUsuario(token);
        return userId.equals(userDetails.getId()) && !estaTokenExpirado(token);
    }

    private boolean estaTokenExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

    private Date extraerFechaExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }
}