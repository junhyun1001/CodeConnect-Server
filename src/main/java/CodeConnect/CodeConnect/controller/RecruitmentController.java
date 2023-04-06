package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.EditRecruitmentDto;
import CodeConnect.CodeConnect.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruitments")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    // 게시글 쓰기
    @PostMapping("/create")
    public ResponseDto<Recruitment> writePost(@RequestBody CreateRecruitmentDto createRequestDto, @AuthenticationPrincipal String email) {
        return recruitmentService.createPost(createRequestDto, email);
    }

    // 게시글 전체 조회
    @GetMapping("/list")
    public ResponseDto<List<Recruitment>> getPosts(@AuthenticationPrincipal String email) {
        return recruitmentService.getAllPosts(email);
    }

    // 게시글 수정 -> 게시글 id를 가지고 찾아야됨 (http://localhost:8080/recruitment/edit/1) 이렇게 주소 주면 됨
    // 클라이언트에서 해당 게시글의 id를 주소에 넣어야됨
    @PutMapping("/edit/{id}")
    public ResponseDto<Recruitment> editPost(@RequestBody EditRecruitmentDto editRequestDto, @PathVariable Long id, @AuthenticationPrincipal String email) {
        return recruitmentService.editPost(editRequestDto, id, email);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseDto<?> deletePost(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return recruitmentService.deletePost(email, id);
    }

}
