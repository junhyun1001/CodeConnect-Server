package CodeConnect.CodeConnect.domain;

import CodeConnect.CodeConnect.converter.FieldConverter;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원 정보를 갖는 클래스
 */

@Entity // Member 클래스를 Entity 클래스로 사용
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

    @NotBlank(message = "주소를 입력해 주세요")
    private String address;

    @Convert(converter = FieldConverter.class)
    private List<String> fieldList;

    /**
     *  회원과 모집 게시글은 1:N 관계이다.
     *  즉, 한 명의 회원은 여러개의 모집 게시글을 작성할 수 있지만, 각 모집 게시글은 한 명의 회원에 의해서 작성된다.
     *
     * CascadeType.ALL: CascadeType.ALL은 해당 엔티티가 저장, 갱신, 삭제, 병합 등 모든 작업에 대해 연관된 엔티티들도 모두 해당 작업을 수행하도록 하는 옵션
     * 즉, 영속성 컨텍스트에서 해당 엔티티가 상태 변화를 하면, 연관된 모든 엔티티도 같은 상태 변화를 하게된다.
     *
     * orphanRemoval = true: 부모 엔티티에서 자식 엔티티를 삭제하고 해당 엔티티를 데이터베이스에서 삭제시킨다.
     */

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // 해당 필드에 대한 데이터를 무시하고 객체가 생성됨
    private List<Recruitment> recruitments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Qna> qnas = new ArrayList<>();

    public Member(SignUpRequestDto dto) {

        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
        this.createMemberTime = dto.getCreateMemberTime();
        this.address = dto.getAddress();
        this.fieldList = dto.getFieldList();
    }

}
