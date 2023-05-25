package CodeConnect.CodeConnect.dto.member;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateMemberRequestDto {

    private String nickname;

    private String address;

    private List<String> fieldList;

    private String base64Image;

}
