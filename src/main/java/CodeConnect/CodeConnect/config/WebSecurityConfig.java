package CodeConnect.CodeConnect.config;

import CodeConnect.CodeConnect.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and() // cors 정책 (현재는 Application에서 설정 해놓음)
                .csrf().disable() // csrf 대책 (현재는 비활성화)
                .httpBasic().disable() // Basic 인증 (현재는 Bearer 인증 방법을 사용함)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // 세션 기반 인증을 비활성화 시킴
                .authorizeRequests().antMatchers("/", "/members/**").permitAll() // '/', '/members 모듈에 대해서는 모두 허용(인증을 사용하지 않고 사용 가능하게 함)
                .anyRequest().authenticated(); // 나머지 Request에 대해서는 모두 인증된 사용자만 가능하게 함

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
