package CodeConnect.CodeConnect.domain;

import CodeConnect.CodeConnect.converter.FieldConverter;
import CodeConnect.CodeConnect.dto.SignUpRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

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
    private String email; // 이메일

    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String password; // 비밀번호

    @NotBlank(message = "닉네임을 입력해 주세요")
    private String nickname;

    private LocalDateTime createMemberTime;

    private String address;

    @Convert(converter = FieldConverter.class)
    private List<String> fieldList;

    public Member(SignUpRequestDto dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
        this.createMemberTime = dto.getCreateMemberTime();
        this.address = dto.getAddress();
        this.fieldList = dto.getFieldList();
    }

}
