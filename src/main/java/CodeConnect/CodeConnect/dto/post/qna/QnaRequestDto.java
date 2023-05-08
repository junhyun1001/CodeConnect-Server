package CodeConnect.CodeConnect.dto.post.qna;

import lombok.Data;
import lombok.NoArgsConstructor;

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
}