package CodeConnect.CodeConnect.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


/**
 * JWT 생성 및 검증을 위한 키
 */

@Service
@AllArgsConstructor
public class TokenProvider {

    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 암호화
    public String create(String email, String nickname, List<String> fieldList) {


        Date exprTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 만료날짜를 현재 시간으로부터 +1시간

        return Jwts.builder()
                .setSubject(email) // 토큰 제목
//                .setAudience(nickname) // 토큰 대상자
//                .claim("field", fieldList) // 관심분야
                .setIssuedAt(new Date()) // 토큰 생성 시간
                .setExpiration(exprTime) // 토큰 만료 시간
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 디코딩
    public Claims validate(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
