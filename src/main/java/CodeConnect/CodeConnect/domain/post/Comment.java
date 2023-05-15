package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId; // 댓글 id

    private String comment; // 댓글 내용
    
    private String nickname; // 회원 닉네임

    /**
     * 해당 댓글을 통해 회원을 조회할 수 있음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Member member; // 회원 조회

    private String currentDateTime; // 댓글 작성 시간
    private String modifiedDateTime;
    @Getter @Setter
    private String role;
    @Column(name = "cocomment_count")
    private Integer cocommentCount = 0; // 대댓글 개수
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 user가 삭제되면 같이 삭제됨
    private Qna qna;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("currentDateTime ASC") //comment 오름차순
    @JsonIgnore
    private final List<Cocomment> cocomments = new ArrayList<>();

    public void setQna(Qna qna){
        this.qna = qna;
        if(qna != null){
            qna.getComments().add(this);
            this.commentId=1L;
        }
    }


    public Comment( String nickname, String comment){
        this.nickname = nickname;
        this.comment = comment;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        this.setCurrentDateTime(String.valueOf(LocalDateTime.now().format(formatter)));
    }


}
