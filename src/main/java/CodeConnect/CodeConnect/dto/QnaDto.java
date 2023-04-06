package CodeConnect.CodeConnect.dto;

import CodeConnect.CodeConnect.domain.post.Qna;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QnaDto {
    private Long qnaId;
    private String title; //제목
    private String content; //내용

    public QnaDto(Qna qna){
        this.qnaId = qna.getQnaId();
        this.title = qna.getTitle();
        this.content = qna.getContent();
    }
}
