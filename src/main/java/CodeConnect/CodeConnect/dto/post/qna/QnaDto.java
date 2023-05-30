package CodeConnect.CodeConnect.dto.post.qna;

import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.post.Qna;
import lombok.Getter;

@Getter
public class QnaDto {

    private final Long qnaId;

    private final String title;

    private final String content;

    private final String nickname;

    private final String currentDateTime;

    private final String modifiedDateTime;

    private final int commentCount;

    private final String imagePath;

    private final String profileImagePath;

    private final int likeCount;

    public QnaDto(Qna qna) {
        this.qnaId = qna.getQnaId();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.nickname = qna.getNickname();
        this.currentDateTime = TimeUtils.formatTimeAgo(qna.getCurrentDateTime());
        this.modifiedDateTime = qna.getModifiedDateTime();
        this.commentCount = qna.getCommentCount();
        this.imagePath = qna.getImagePath();
        this.profileImagePath = qna.getMember().getProfileImagePath();
        this.likeCount = qna.getLikeCount();
    }

}
