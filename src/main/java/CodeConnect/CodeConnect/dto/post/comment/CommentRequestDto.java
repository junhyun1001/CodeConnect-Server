package CodeConnect.CodeConnect.dto.post.comment;

import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {

    private String comment;
    private Long commentId;  // 새로 추가한 필드
    private String title;
    // 생성자, setter/getter 메서드 등
    // ...

    public Long getCommentId() {
        if (commentId == null) {
            return 0L; // 댓글 작성 시 commentId가 지정되지 않은 경우 0으로 처리
        }
        return commentId;
    }
}