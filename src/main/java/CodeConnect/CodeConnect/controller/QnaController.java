package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.ResponseDto;
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
    public ResponseDto<List<Qna>> getQnaList() {
        return qnaService.findQna();
    }

    //상세조회
    @GetMapping("/detail/{qnaId}")
    public ResponseDto<Map<Role, Object>> qna_detail(@PathVariable("qnaId") Long qnaId, @AuthenticationPrincipal String email) {
        return qnaService.findOne(qnaId, email);
    }

    //생성
    @PostMapping("/create")
    public ResponseDto<Qna> writeQna(@RequestBody QnaRequestDto dto, @AuthenticationPrincipal String email) {
        return qnaService.writeQna(dto, email);
    }

    //
    @PutMapping("/update/{qnaId}")
    public ResponseDto<Qna> qna_update(@PathVariable("qnaId") Long qnaId, @RequestBody QnaRequestDto qnaDto, @AuthenticationPrincipal String email) {
        return qnaService.update(qnaId, qnaDto.getTitle(), qnaDto.getContent(),qnaDto.getBase64Image(), email);
    }

    //삭제
    @DeleteMapping("/delete/{qnaId}")
    public ResponseDto<String> qna_delete(@RequestBody @PathVariable("qnaId") Long qnaId, @AuthenticationPrincipal String email) {
        return qnaService.delete(qnaId, email);
    }

    //검색
    @GetMapping("/search/{text}")
    public ResponseDto<List<Qna>> textSearch(@PathVariable String text) {
        return qnaService.search(text);
    }


}