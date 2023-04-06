package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Qna")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Qna extends Post {

    @Id
    @GeneratedValue
    private Long qnaId;

    /**
     * 작성자 정보에 대한 매핑 정보를 통해 작성자(Member) 엔티티를 참조할 수 있다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    @JsonIgnore
    private Member member;

    /**
     * 하나의 게시글이 여러개의 댓글과 관계를 가지므로 1:N 관계를 사용.
     */
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private final List<Comment> comments = new ArrayList<>();

    public Qna(QnaRequestDto dto, String nickname) {
        super.title = dto.getTitle();
        super.content = dto.getContent();
        super.nickname = nickname;
    }
    // 댓글 추가 메서드
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setQna(this);
    }

}
