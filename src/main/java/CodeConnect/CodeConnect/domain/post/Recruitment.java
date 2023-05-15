package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.UpdateRecruitmentDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Recruitment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Recruitment extends Post {

    @Id
    @GeneratedValue
    private Long recruitmentId; // 모집 게시글 id

    // 작성자 정보에 대한 매핑 정보를 통해 작성자(Member) 엔티티를 참조할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Member member; // 회원 정보

    private String address; // 작성자 주소

    private int count; // 해당 게시글의 제한 인원수

    private int currentCount; // 해당 게시글의 현재 참가인원

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "recruitment_id"))
    @JsonIgnore
    private List<String> currentParticipantMemberList;

    private String field; // 관심분야

    @OneToOne(mappedBy = "recruitment")
    @JsonIgnore
    private ChatRoom chatRoom;

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
    public void updatePost(UpdateRecruitmentDto dto) {
        setTitle(dto.getTitle());
        setContent(dto.getContent());
        setCount(dto.getCount());
        setField(dto.getField());
        setModifiedDateTime(changeDateTimeFormat(LocalDateTime.now()));
    }

}