package CodeConnect.CodeConnect.dto.member;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditMemberDto {

    private String nickname;

    private String address;

    private List<String> fieldList;
}
