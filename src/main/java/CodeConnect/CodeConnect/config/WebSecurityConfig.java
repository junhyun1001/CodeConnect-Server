package CodeConnect.CodeConnect.config;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and() // CORS 정책을 활성화 시킴(main 메소드에서 정의함)
                .csrf().disable() // csrf 정책 (현재는 비활성화)
                .httpBasic().disable() // Basic 인증 (현재는 Bearer 인증 방법을 사용함)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // 세션 기반 인증을 비활성화 시킴
                .authorizeRequests().antMatchers(ApiPaths.ALLOWED_API).permitAll() // 접근 허용할 api
                .anyRequest().authenticated() // 나머지 Request에 대해서는 모두 인증된 사용자만 가능하게 함
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // jwt 필터에서 예외가 발생(헤더에 토큰이 담겨있지 않거나, 토큰이 만료 또는 조작 된 경우)한 경우
                .and().addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            String jsonResponse = new ObjectMapper().writeValueAsString(ResponseDto.setFail(authException.getMessage()));
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}