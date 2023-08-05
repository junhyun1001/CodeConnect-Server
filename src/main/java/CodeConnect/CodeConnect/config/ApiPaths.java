package CodeConnect.CodeConnect.config;

public class ApiPaths {
    public static final String[] ALLOWED_API = {
            // 홈, 회원가입, 로그인
            "/",
            "/members/signup",
            "/members/login",
            // swagger
            "/v3/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**"
    };
}