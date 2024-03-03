package ru.shaikhraziev.bankingservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shaikhraziev.bankingservice.dto.UserReadDto;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    public String generateToken(UserReadDto user) {
        Map<String, Object> header = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        String alg = "HS256";
        String typ = "JWT";

        Long userId = user.getId();

        header.put("alg", alg);
        header.put("typ", typ);

        payload.put("userId", userId);

        return Jwts.builder()
                .setHeader(header)
                .claims(payload)
                .signWith(
                        SignatureAlgorithm.HS256,
                        Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))
                )
                .compact();
    }

    public boolean rightsVerification(String JWT, Long id) {
        if (isTokenValid(JWT)) {
            Object o = extractPayload(JWT).getOrDefault("userId", "error");

            return o.toString().equals(String.valueOf(id));
        }

        return false;
    }

    public boolean isTokenValid(String actualToken) {
        JwsHeader jwsHeader = extractHeader(actualToken);
        Claims payload = extractPayload(actualToken);

        String expectedToken = Jwts.builder()
                .setHeader(jwsHeader)
                .claims(payload)
                .signWith(
                        SignatureAlgorithm.HS256,
                        Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))
                )
                .compact();

        return expectedToken.equals(actualToken);
    }

    public JwsHeader extractHeader(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getHeader();
    }

    public Claims extractPayload(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }
}