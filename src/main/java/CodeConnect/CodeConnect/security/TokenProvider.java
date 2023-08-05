package CodeConnect.CodeConnect.security;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.token.Token;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class TokenProvider {

    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    // 암호화
    public Token create(String email) {
        Date now = new Date();
        Date accessExprTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 만료날짜를 현재 시간으로부터 +1시간
        Date refreshExprTime = Date.from(Instant.now().plus(14, ChronoUnit.DAYS));

        String accessToken = Jwts.builder()
                .setSubject(email) // 토큰 제목
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(accessExprTime) // 토큰 만료 시간
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject("Refresh Token") // 토큰 제목
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(refreshExprTime) // 토큰 만료 시간
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();


        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    // 토큰 디코딩
    public String validateToken(String token) {
        // redis에 등록되어있는 키일 경우
        if (redisUtil.hasKeyBlackList(token)) {
            throw new RuntimeException("로그아웃 된 키 입니다.");
        }

        // access toekn의 sub(email)을 리턴한다.
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String validateRefreshToken(String refreshToken) {

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(refreshToken);

            Optional<Member> optionalMember = memberRepository.findById(claims.getBody().getSubject());
            if (optionalMember.isEmpty()) {
                return "존재하지 않는 회원입니다.";
            }
            Member member = optionalMember.get();
            // refresh token의 만료기간이 지나지 않았을 경우, 새로운 access token 발급
            if (!claims.getBody().getExpiration().before(new Date()) && member.getRefreshToken().equals(refreshToken)) {
                return create(claims.getBody().getSubject()).getAccessToken();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return null;
    }
}
