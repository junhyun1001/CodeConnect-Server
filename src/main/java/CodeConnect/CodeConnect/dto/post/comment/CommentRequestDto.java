package CodeConnect.CodeConnect.dto.post.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
    public String email;
    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    private String comment;
}