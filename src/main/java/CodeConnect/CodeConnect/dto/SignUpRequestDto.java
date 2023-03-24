package CodeConnect.CodeConnect.dto;

import CodeConnect.CodeConnect.domain.Address;
import CodeConnect.CodeConnect.domain.Field;
import CodeConnect.CodeConnect.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 클라이언트에서 서버로 요청을 받는 DTO
 *
 * 회원가입에 성공하면 reuslt(true, false), message, data{token, exprTime}을 반환한다.
 */

@Getter
@Setter
public class SignUpRequestDto {

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$") // 영문, 숫자, 특수문자 8~10자리
    private String password;
    private String passwordCheck;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{12}$") // 한글, 영문, 숫자 12자리만 사용 가능
    private String nickname;

    private LocalDateTime createMemberTime = LocalDateTime.now();

    private String state;
    private String city;
    private String street;

    private Address address;

    private Field field;



}
