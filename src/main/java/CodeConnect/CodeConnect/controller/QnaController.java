package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import CodeConnect.CodeConnect.service.QnaService;
import CodeConnect.CodeConnect.service.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnaController {
    private final QnaService qnaService;

    //전체조회
    @GetMapping("/list")
    public ResponseDto<List<QnaDto>> getQnaList() {
        return qnaService.findQna();
    }

    //상세조회
    @GetMapping("/detail/{qnaId}")
    public ResponseDto<Map<Role, Object>> qna_detail(@PathVariable("qnaId") Long qnaId, @AuthenticationPrincipal String email) {
        return qnaService.findOne(qnaId, email);
    }

    //생성
    @PostMapping("/create")
    public ResponseDto<QnaDto> writeQna(@RequestBody QnaRequestDto dto, @AuthenticationPrincipal String email) {
        return qnaService.writeQna(dto, email);
    }

    //업데이트
    @PutMapping("/update/{qnaId}")
    public ResponseDto<QnaDto> qna_update(@PathVariable("qnaId") Long qnaId, @RequestBody QnaRequestDto qnaDto, @AuthenticationPrincipal String email) {
        return qnaService.update(qnaId, qnaDto.getTitle(), qnaDto.getContent(), qnaDto.getBase64Image(), email);
    }

    //삭제
    @DeleteMapping("/delete/{qnaId}")
    public ResponseDto<String> qna_delete(@RequestBody @PathVariable("qnaId") Long qnaId, @AuthenticationPrincipal String email) {
        return qnaService.delete(qnaId, email);
    }

    //검색
    @GetMapping("/search/{text}")
    public ResponseDto<List<QnaDto>> textSearch(@PathVariable String text) {
        return qnaService.search(text);
    }

    //top10 조회
    @GetMapping("/popular")
    public ResponseDto<List<QnaDto>> getPopularPost() {
        return qnaService.getPopularPost();
    }

    //게시글 좋아요 카운팅
    @PutMapping("/like/{qnaId}")
    public ResponseDto<Integer> likeCounting(@AuthenticationPrincipal String email, @PathVariable Long qnaId) {
        return qnaService.likeCounting(email, qnaId);
    }
}