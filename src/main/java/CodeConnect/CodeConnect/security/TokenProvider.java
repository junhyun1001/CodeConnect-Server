package CodeConnect.CodeConnect.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 * JWT 생성 및 검증을 위한 키
 */

@Service
public class TokenProvider {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 암호화
    public String create(String email) {
        Date exprTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 만료날짜를 현재 시간으로부터 +1시간

        String token = Jwts.builder()
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(exprTime)
                .compact();
        return token;
    }

    // 토큰 디코딩
    public String validate(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}
