package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.EditRecruitmentDto;
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
        super.setCurrentDateTime(changeDateTimeFormat(LocalDateTime.now()));
        this.address = address;
        this.count = dto.getCount();
        this.field = dto.getField();
    }

    // 게시글 정보 업데이트
    public void updatePost(EditRecruitmentDto dto) {
        setTitle(dto.getTitle());
        setContent(dto.getContent());
        setCount(dto.getCount());
        setField(dto.getField());
        setModifiedDateTime(changeDateTimeFormat(LocalDateTime.now()));
    }

}