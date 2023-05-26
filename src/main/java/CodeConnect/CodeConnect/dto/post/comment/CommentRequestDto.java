package CodeConnect.CodeConnect.dto.post.comment;


import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Post;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {

    private Long qnaId;
    private Long commentId;
    private String comment;
    private int cocommentCount;
    private String currentDateTime;
    private String modifiedDateTime;
    private String profileImagePath; //회원 프로필 사진 경로

    public Long getCommentId() {
        if (commentId == null) {
            return 0L; // 댓글 작성 시 commentId가 지정되지 않은 경우 0으로 처리
        }
        return commentId;
    }
    public CommentRequestDto(Comment comment) {
        this.qnaId = comment.getQna().getQnaId();
        this.commentId = comment.getCommentId();
        this.comment = comment.getComment();
        this.cocommentCount = comment.getCocomments().size();
        this.currentDateTime = comment.getCurrentDateTime();
        this.modifiedDateTime = comment.getModifiedDateTime();
        this.profileImagePath = comment.getMember().getProfileImagePath();
    }
}