package CodeConnect.CodeConnect.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${resource.image.path}")
    private String resourcePath;

    @Value("${upload.image.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 기본 프로필
        registry.addResourceHandler(uploadPath + "/member/default/**")
                .addResourceLocations(resourcePath + "/member/default/");

        // 회원 프로필 수정
        registry.addResourceHandler(uploadPath + "/member/profile/**")
                .addResourceLocations(resourcePath + "/member/profile/");

        // qna 이미지 업로드
        registry.addResourceHandler(uploadPath + "/qna/**")
                .addResourceLocations(resourcePath + "/qna/");

    }
}