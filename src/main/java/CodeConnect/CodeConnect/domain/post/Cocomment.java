package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cocomment")
@Getter
@Setter
@NoArgsConstructor
public class Cocomment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cocommentId; // 댓글 id

    @NotBlank
    private String cocomment; // 댓글 내용

    private String nickname; // 회원 닉네임

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Member member; // 회원 조회

    private String currentDateTime; // 댓글 작성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 user가 삭제되면 같이 삭제됨
    private Comment comment;

    private String profileImagePath; //회원 프로필 사진 경로

    public void setComment(Comment comment){
        this.comment = comment;
        if(comment != null){
            comment.getCocomments().add(this);
            this.cocommentId=1L;
        }
    }
    public Cocomment( String nickname, String cocomment){
        this.nickname = nickname;
        this.cocomment = cocomment;
        this.setCurrentDateTime(TimeUtils.changeDateTimeFormat(LocalDateTime.now()));
    }
}
