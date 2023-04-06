package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Recruitment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recruitment extends Post {

    @Id
    @GeneratedValue
    private Long recruitmentId; // 모집 게시글 id

    /**
     * 작성자 정보에 대한 매핑 정보를 통해 작성자(Member) 엔티티를 참조할 수 있다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Member member; // 회원 정보

    private String address; // 작성자 주소

    private int count; // 인원수

    @Enumerated(EnumType.STRING)
    private Role role; // 방장인지 참여자인지 구분

    private String field; // 관심분야

    // 연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getRecruitments().add(this);
    }

    // 생성자
    public Recruitment(CreateRecruitmentDto dto, String nickname, String address) {
        super.title = dto.getTitle();
        super.content = dto.getContent();
        super.nickname = nickname;
        this.address = address;
        this.setCurrentDateTime(LocalDateTime.now());
        this.count = dto.getCount();
        this.field = dto.getField();
        this.role = dto.getRole();
    }

    // 참여하기 버튼을 눌렀을 때 DB도 하나 더 만들어야됨

}