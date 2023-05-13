package CodeConnect.CodeConnect.filter;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    // Request Header의 Authorization 필드에서 Bearer Token을 가져와 검증하는 역할
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = parseBearerToken(request);

        try {
            if (token != null && !token.equalsIgnoreCase("null")) {
                String email = tokenProvider.validate(token);

                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            log.error(e.getMessage());

            // 유효성 검증에 실패한 경우도 SecurityContext에 인증 정보를 추가해야 함
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null, AuthorityUtils.NO_AUTHORITIES);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

            // 오류 응답 객체 생성 및 데이터 설정
            ObjectMapper objectMapper = new ObjectMapper();

            // 오류 응답 객체를 JSON으로 변환
            String jsonResponse = objectMapper.writeValueAsString(ResponseDto.setFail("유효하지 않은 토큰입니다."));

            // 응답 객체를 설정
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            response.getWriter().close();

            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }

}

