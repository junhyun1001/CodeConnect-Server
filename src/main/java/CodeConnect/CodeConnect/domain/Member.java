package CodeConnect.CodeConnect.domain;

import CodeConnect.CodeConnect.dto.SignUpRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 회원 정보를 갖는 클래스
 */

@Entity(name = "Member") // Member 클래스를 Entity 클래스로 사용
@Table(name = "Member") // Entity와 매핑할 테이블 지정
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @Email
    private String email; // 이메일

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자 8~10자리만 가능합니다.") // 영문, 숫자, 특수문자 8~10자리
    private String password; // 비밀번호
    private String passwordCheck; // 비밀번호 확인

    @NotBlank(message = "닉네임을 입력해 주세요")
    @Column(unique = true)
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{1,18}$", message = "닉네임은 한글, 영문, 숫자 18자리 이하만 사용 가능합니다.") // 한글, 영문, 숫자 12자리만 사용 가능
    private String nickname;

    private LocalDateTime createMemberTime;

    @Enumerated(EnumType.STRING)
    private Field field; // 사용자 관심 분야

    @Embedded
    private Address address; // 사용자 주소

    public Member(SignUpRequestDto dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.passwordCheck = dto.getPasswordCheck();
        this.nickname = dto.getNickname();
        this.createMemberTime = dto.getCreateMemberTime();
        this.address = dto.getAddress();
        this.field = dto.getField();
    }

}
