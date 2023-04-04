package CodeConnect.CodeConnect.controller;


import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.EditRecruitmentDto;
import CodeConnect.CodeConnect.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    // 게시글 쓰기
    @PostMapping("/create")
    public ResponseDto<Recruitment> writePost(@RequestBody CreateRecruitmentDto createRequestDto, @AuthenticationPrincipal String email) {
        return recruitmentService.createPost(createRequestDto, email);
    }

    // 게시글 수정
//    @PutMapping("/edit")
//    public ResponseDto<Recruitment> editPost(@RequestBody EditRecruitmentDto editRequestDto, @AuthenticationPrincipal String email) {
//        return recruitmentService.editPost(editRequestDto, email);
//    }

    // 게시글 삭제

}
