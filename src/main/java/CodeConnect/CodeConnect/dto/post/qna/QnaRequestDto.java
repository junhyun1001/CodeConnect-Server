package CodeConnect.CodeConnect.dto.post.qna;

import CodeConnect.CodeConnect.domain.post.Post;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.repository.QnaRepository;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class QnaRequestDto{
    private Long qnaId;
    private String nickname;
    private String title;
    private String content;
    private int commentCount;
    private String currentDateTime;
    private String modifiedDateTime;

    public QnaRequestDto(Qna qna){
        this.qnaId = qna.getQnaId();
        this.nickname = qna.getNickname();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.commentCount = qna.getCommentCount();
        this.currentDateTime = qna.getCurrentDateTime();
        this.modifiedDateTime = qna.getModifiedDateTime();
    }
}