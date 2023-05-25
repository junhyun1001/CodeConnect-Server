package CodeConnect.CodeConnect.dto.member;

import CodeConnect.CodeConnect.converter.Base64Converter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UpdatedMemberResponseDto {

    private String nickname;

    private String address;

    private List<String> fieldList;

    private String profileImagePath;

    public UpdatedMemberResponseDto(UpdateMemberRequestDto updateMemberRequestDto, String profileImagePath) {
        this.nickname = updateMemberRequestDto.getNickname();
        this.address = updateMemberRequestDto.getAddress();
        this.fieldList = updateMemberRequestDto.getFieldList();
        this.profileImagePath = profileImagePath;
    }

}
