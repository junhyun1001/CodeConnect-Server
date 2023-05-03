package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentRequestDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentResponseDto;
import CodeConnect.CodeConnect.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/create/{qnaId}")
    public ResponseDto<Comment> createComment(@PathVariable("qnaId") Long qnaId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal String email) {
        return commentService.createComment(qnaId, requestDto, email);
    }

    // 댓글 조회(front에서 해당 qna에 대한 댓글표시 눌렀을 때
    @GetMapping("/detail/{qnaId}")
    public ResponseDto<List<CommentResponseDto>> detailComment(@PathVariable("qnaId") Long qnaId, @AuthenticationPrincipal String email) {
        return commentService.findComment(qnaId, email);
    }

    //댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal String email) {
        return commentService.deleteComment(commentId, email);
    }

    //댓글 업데이트
    @PutMapping("/update/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentResponseDto commentResponseDto, @AuthenticationPrincipal String email) {
        return commentService.updateComment(commentId, email, commentResponseDto.getComment());
    }


}
