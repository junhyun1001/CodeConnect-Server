package CodeConnect.CodeConnect.dto.post.comment;

import CodeConnect.CodeConnect.domain.post.Cocomment;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CocommentRequestDto {
    private Long qnaId;
    private Long cocommentId;
    private String cocomment;
    private String nickname;
    private String currentDateTime;
    private String modifiedDateTime;

    private String profileImagePath; //회원 프로필 사진 경로

    public Long getCocommentId() {
        if (cocommentId == null) {
            return 0L; // 댓글 작성 시 cocommentId가 지정되지 않은 경우 0으로 처리
        }
        return cocommentId;
    }

    public CocommentRequestDto(Cocomment cocomment) {
        this.qnaId = cocomment.getComment().getQna().getQnaId();
        this.cocommentId = cocomment.getCocommentId();
        this.cocomment = cocomment.getCocomment();
        this.nickname = cocomment.getNickname();
        this.currentDateTime = cocomment.getCurrentDateTime();
        this.modifiedDateTime = cocomment.getModifiedDateTime();
        this.profileImagePath = cocomment.getMember().getProfileImagePath(); // 추가된 부분
    }

}
