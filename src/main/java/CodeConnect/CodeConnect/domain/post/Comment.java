package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private Long commentId; // 댓글 id

    private String comment; // 댓글 내용

    /**
     * 해당 댓글을 통해 회원을 조회할 수 있음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private Member member; // 회원 조회

    private LocalDateTime currentDateTime; // 댓글 작성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    @JsonIgnore
    private Qna qna;

}
