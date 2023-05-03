package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    /**
     * 작성자 정보에 대한 매핑 정보를 통해 작성자(Member) 엔티티를 참조할 수 있다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Member member;
    @Column(name = "comment_count")
    private int commentCount; // 댓글 개수

    // 연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getQnas().add(this);
    }

     /**
     * 하나의 게시글이 여러개의 댓글과 관계를 가지므로 1:N 관계를 사용.
     */
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("currentDateTime ASC") //qna 오름차순
    private final List<Comment> comments = new ArrayList<>();

    public Qna(QnaRequestDto dto,String nickname, String title, String content){
        super.title = title;
        super.nickname = nickname;
        super.content = content;
        this.commentCount = dto.getCommentCount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        super.setCurrentDateTime(String.valueOf(LocalDateTime.now().format(formatter)));
    }


}
