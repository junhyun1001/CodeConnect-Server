package CodeConnect.CodeConnect.dto.post.comment;

import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "set")
public class CommentResponseDto {

    private Long qnaId;
    private Long commentid;
    private String comment;

    public CommentResponseDto(Comment comment){
        this.qnaId = comment.getQna().getQnaId();
        this.commentid = comment.getCommentId();
        this.comment = comment.getComment();

    }

//    public static CommentResponseDto setSuccess(Long qnaId, String message,Comment comment) {
//
//        CommentResponseDto dto = new CommentResponseDto();
//        dto.qnaId = qnaId;
//        dto.message = message;
//        dto.commentid = comment.getQna().getCommentCount();
//        dto.comment = comment.getComment();
//        return dto;
//    }
//    public static CommentResponseDto setFail(String message) {
//
//        return CommentResponseDto.setFail(message);
//    }
//
//    public static CommentResponseDto setSuccess_detail(Long qnaId,String message, List<CommentRequestDto> comment) {
//        CommentResponseDto dto = new CommentResponseDto();
//        dto.qnaId = qnaId;
//        dto.message = message;
//        dto.comments = comment;
//        return dto;
//
//    }
//
//    public static CommentResponseDto setSuccess(String message) {
//
//        return CommentResponseDto.setFail(message);
//    }
}
