package CodeConnect.CodeConnect.dto;

import CodeConnect.CodeConnect.domain.Field;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

/**
 * 클라이언트에서 서버로 요청을 받는 DTO
 *
 * 회원가입에 성공하면 reuslt(true, false), message, data를 반환한다.
 */

@Getter
@Setter
public class SignUpRequestDto {

    @Email
    private String email;

    private String password;
    private String passwordCheck;

    private String nickname;

    private LocalDateTime createMemberTime = LocalDateTime.now();

    private String state;
    private String city;
//    private String street;

    private Field field;

}
