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
                String email = tokenProvider.validateToken(token);

                // 토큰의 subject가 "Refresh Token"이면 사용 불가능
                if (email.equals("Refresh Token")) {
                    jwtExceptionHandler(response, "Refresh Token은 사용할 수 없습니다.");
                    return;
                }

                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            // 오류 객체 반환
            jwtExceptionHandler(response, e.getMessage());
            log.error(e.getMessage());
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

    private void jwtExceptionHandler(@NotNull HttpServletResponse response, String message) throws IOException {
        String jsonResponse = new ObjectMapper().writeValueAsString(ResponseDto.setFail(message));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        response.getWriter().close();
    }

}

