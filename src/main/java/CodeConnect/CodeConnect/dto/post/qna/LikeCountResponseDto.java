package CodeConnect.CodeConnect.dto.post.qna;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LikeCountResponseDto {

    private final boolean liked;

    private final int likeCount;

}
