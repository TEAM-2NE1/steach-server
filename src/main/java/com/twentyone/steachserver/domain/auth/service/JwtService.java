package com.twentyone.steachserver.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    public static long accessTokenValidTime = Duration.ofMinutes(200).toMillis(); // 만료시간 30분

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {  //id추출
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            String tokenType //access, refresh
    ) {
        long durationTime = accessTokenValidTime;

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + durationTime))
                .claim("token_type", tokenType)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, "access");
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        boolean b = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

        //리프레쉬 토큰인 경우 인증처리 ㄴㄴ
        if (extractClaim(token, (Claims claim) -> claim.get("token_type", String.class)).equals("refresh")) {
            return false;
        }

        return b;
    }

    public boolean isTokenExpired(String token) {
        try {
            return System.currentTimeMillis() > extractExpiration(token).getTime();// 현재 시간이 토큰의 만료 시간 이전인지 확인
        } catch (Exception e) {
            return false;
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
