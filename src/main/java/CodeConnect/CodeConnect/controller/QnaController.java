package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.QnaDto;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentRequestDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import CodeConnect.CodeConnect.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnaController {
    private final QnaService qnaService;
    //전체조회
    @GetMapping("/list")
    public ResponseDto<List<QnaDto>> getQnaList(){
        return qnaService.findQna();
    }
    //상세조회
    @GetMapping("/detail/{qnaId}")
    public ResponseDto<Qna> qna_detail(@PathVariable("qnaId") Long qnaId){
        return qnaService.findOne(qnaId);
    }
    //생성
    @PostMapping("/create")
    public ResponseDto<Qna> writeQna(@RequestBody QnaRequestDto dto, @AuthenticationPrincipal String email) {
        return qnaService.writeQna(dto, email);
    }
    //
    @PutMapping("/update")
    public ResponseDto<?> qna_update(@RequestBody QnaDto qnaDto){
        return qnaService.update(qnaDto.getQnaId(), qnaDto.getTitle(), qnaDto.getContent());
    }
    //삭제
    @DeleteMapping("/delete/{qnaId}")
    public ResponseDto<?> qna_delte(@RequestBody @PathVariable("qnaId") Long qnaId){
        return qnaService.delete(qnaId);
    }

    //댓글 생성
//    @PostMapping("/{qnaId}/comment")
//    public ResponseDto<?> addComment(@PathVariable Long qnaId,
//                                     @RequestBody CommentRequestDto commentRequestDto,@AuthenticationPrincipal String email) {
//        return qnaService.addComment(qnaId, commentRequestDto,email);
//
//    }
}