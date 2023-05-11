package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Cocomment")
@Getter
@Setter
@NoArgsConstructor
public class Cocomment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cocommentId; // 댓글 id
    private String cocomment; // 댓글 내용

    private String nickname; // 회원 닉네임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Member member; // 회원 조회
    private String currentDateTime; // 댓글 작성 시간
    private String modifiedDateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 user가 삭제되면 같이 삭제됨
    private Comment comment;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        this.setCurrentDateTime(String.valueOf(LocalDateTime.now().format(formatter)));
    }
}