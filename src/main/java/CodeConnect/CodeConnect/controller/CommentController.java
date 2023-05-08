package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentRequestDto;
import CodeConnect.CodeConnect.service.CommentService;
import CodeConnect.CodeConnect.service.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseDto<List<CommentRequestDto>> detailComment(@PathVariable("qnaId") Long qnaId, @AuthenticationPrincipal String email) {
        return commentService.findComment(qnaId, email);
    }

    //댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal String email) {
        return commentService.deleteComment(commentId, email);
    }

    //댓글 업데이트
    @PutMapping("/update/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal String email) {
        return commentService.updateComment(commentId, email, commentRequestDto.getComment());
    }


}
