package CodeConnect.CodeConnect.dto.profile;


import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ProfileQnaDto {

    private List<QnaRequestDto> qnaList;


}
