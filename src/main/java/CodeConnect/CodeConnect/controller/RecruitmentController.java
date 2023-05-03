package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.EditRecruitmentDto;
import CodeConnect.CodeConnect.service.RecruitmentService;
import CodeConnect.CodeConnect.service.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recruitments")
@RequiredArgsConstructor
@Slf4j
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    // 게시글 쓰기
    @PostMapping("/create")
    public ResponseDto<Recruitment> writePost(@RequestBody CreateRecruitmentDto createRequestDto, @AuthenticationPrincipal String email) {
        return recruitmentService.createPost(createRequestDto, email);
    }

    // 메인 화면에서 보여줄 게시글 리스트(로그인한 회원의 주소와 관심분야가 같은것)
    @GetMapping("/main")
    public ResponseDto<List<Recruitment>> getPosts(@AuthenticationPrincipal String email) {
        return recruitmentService.getPostsByAddressAndField(email);
    }

    // 게시글 전체 조회
    @GetMapping("/list")
    public ResponseDto<List<Recruitment>> getPosts() {
        return recruitmentService.getAllPosts();
    }

    // 게시글 단일 조회
    @GetMapping("/{id}")
    public ResponseDto<Map<Role, Object>> getPost(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return recruitmentService.getPost(email, id);
    }

    // 게시글 제목+내용 기준으로 검색
    @GetMapping("/search")
    public ResponseDto<List<Recruitment>> getSearchList(@RequestParam String keyword) {
        return recruitmentService.getContentBySearch(keyword);
    }

    // 주소로 게시글 검색
    @GetMapping("/searchAddress")
    public ResponseDto<List<Recruitment>> getListByAddress(@RequestParam String address) {
        return recruitmentService.getPostsByAddress(address);
    }

    // 게시글 수정
    @PutMapping("/edit/{id}")
    public ResponseDto<Recruitment> editPost(@RequestBody EditRecruitmentDto editRequestDto, @PathVariable Long id, @AuthenticationPrincipal String email) {
        return recruitmentService.editPost(editRequestDto, id, email);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseDto<String> deletePost(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return recruitmentService.deletePost(email, id);
    }

    // 스터디 게시글 참여 인원에 대한 처리
    @PutMapping("participate/{id}")
    public ResponseDto<Recruitment> participate(@AuthenticationPrincipal String email, @PathVariable Long id, @RequestParam Boolean isParticipating) {
        return recruitmentService.participate(email, id, isParticipating);
    }

}