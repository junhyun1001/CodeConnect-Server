package CodeConnect.CodeConnect.dto.member;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.service.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Email;
import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ProfileDto {
    @Email
    private String email;

    private String nickname;

    private String address;

    private List<String> fieldList;

    private Role role;

    public ProfileDto(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.address = member.getAddress();
        this.fieldList = member.getFieldList();
    }
}
