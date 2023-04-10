package CodeConnect.CodeConnect.dto.post.recruitment;

import CodeConnect.CodeConnect.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecruitmentDto extends Post {

    private int count; // 인원수

    private String field; // 관심분야

}
