package CodeConnect.CodeConnect.dto.post.recruitment;

import CodeConnect.CodeConnect.domain.post.Post;
import CodeConnect.CodeConnect.domain.post.Role;
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

    private Role role; // 방장인지 참여자인지 구분

    private String field; // 관심분야

}
