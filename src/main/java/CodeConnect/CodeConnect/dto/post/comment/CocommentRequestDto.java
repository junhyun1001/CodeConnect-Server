package CodeConnect.CodeConnect.dto.post.comment;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CocommentRequestDto {
    private Long qnaId;
    private Long cocommentId;
    private String cocomment;

    private String currentDateTime;
    private String modifiedDateTime;

    public Long getCommentId() {
        if (cocommentId == null) {
            return 0L; // 댓글 작성 시 cocommentId가 지정되지 않은 경우 0으로 처리
        }
        return cocommentId;
    }

}
