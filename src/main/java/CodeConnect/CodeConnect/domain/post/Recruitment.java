package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;
import lombok.*;

import javax.persistence.*;

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
    @JoinColumn(name = "member_email")
    private Member member;

    private int count; // 인원수

    private Role role; // 방장인지 참여자인지 구분

    /**
     * 참여하기 버튼을 눌렀을 때 DB도 하나 더 만들어야됨
     */

}