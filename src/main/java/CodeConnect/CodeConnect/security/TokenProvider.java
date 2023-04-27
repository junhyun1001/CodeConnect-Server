package CodeConnect.CodeConnect.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 * JWT 생성 및 검증을 위한 키
 */

@Service
@AllArgsConstructor
@Slf4j
public class TokenProvider {

    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 암호화
    public String create(String email) {
        Date now = new Date();
        Date exprTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 만료날짜를 현재 시간으로부터 +1시간

        return Jwts.builder()
                .setSubject(email) // 토큰 제목
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(exprTime) // 토큰 만료 시간
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 디코딩
    public String validate(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            // 토큰 검증 실패 시 예외 발생
            log.error(e.getMessage());
            return null;
        }
    }

}
