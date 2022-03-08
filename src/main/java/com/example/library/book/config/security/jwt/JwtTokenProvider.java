package com.example.library.book.config.security.jwt;

import com.example.library.book.models.Client;
import com.example.library.book.models.ClientRol;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Log
@Component
public class JwtTokenProvider {
    // Naturaleza del Token!!!
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";

    @Value("${jwt.secret:EnUnLugarDeLaManchaDeCuyoNameNoQuieroAcordarmeNoHaMuchoTiempoQueViviaUnHidalgo}")
    private String jwtSecreto;

    @Value("${jwt.token-expiration:86400}")
    private int jwtDuracionTokenEnSegundos;



    public String generateToken(Authentication authentication) {


        Client user = (Client) authentication.getPrincipal();


        Date tokenExpirationDate = new Date(System.currentTimeMillis() + (jwtDuracionTokenEnSegundos * 1000));

        return Jwts.builder()

                .signWith(Keys.hmacShaKeyFor(jwtSecreto.getBytes()), SignatureAlgorithm.HS512)

                .setHeaderParam("typ", TOKEN_TYPE)

                .setSubject(Long.toString(user.getId()))

                .setIssuedAt(new Date())

                .setExpiration(tokenExpirationDate)

                .claim("fullname", user.getName())

                .claim("roles", user.getRoles().stream()
                        .map(ClientRol::name)
                        .collect(Collectors.joining(", "))
                )
                .compact();

    }


    public Long getUserIdFromJWT(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public boolean validateToken(String authToken) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.info("Error en la firma del token JWT: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.info("Token malformado: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.info("El token ha expirado: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.info("Token JWT no soportado: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims vacío");
        }
        return false;
    }
}