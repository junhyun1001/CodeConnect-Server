package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final RecruitmentService recruitmentService;

    // 메인 화면에서 보여줄 게시글 리스트(로그인한 회원의 주소와 관심분야가 같은것. 현재는 같은 주소만 보여줌)
    @GetMapping("/main")
    public ResponseDto<List<Recruitment>> getPosts(@AuthenticationPrincipal String email) {
        return recruitmentService.getPostsByAddressAndField(email);
    }
}
