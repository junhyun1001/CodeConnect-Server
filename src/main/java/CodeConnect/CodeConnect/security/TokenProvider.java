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

    private static final String SECURITY_KEY = "jwtseckey!@";

    // 암호화
    public String create(String email) {
        Date exprTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 만료날짜를 현재 시간으로부터 +1시간

        Key key = Keys.hmacShaKeyFor(SECURITY_KEY.getBytes());

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, key) // 암호화에 사용될 알고리즘과 Key
                .setSubject(email).setIssuedAt(new Date()).setExpiration(exprTime) // JWT 제목, 생성일, 만료일
                .compact(); // 생성
    }

    // 복호화
    public String validate (String token) {
        Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody(); // 매개변수로 받은 토큰을 복호화
        return claims.getSubject(); // 복호화된 토큰의 payload에서 제목을 가져옴
    }

}
