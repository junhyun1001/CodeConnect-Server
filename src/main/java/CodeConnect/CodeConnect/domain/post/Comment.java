package CodeConnect.CodeConnect.domain.post;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Comment")
@Table(name = "Comment")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private Long commentId; // 댓글 id

    private String comment; // 댓글 내용

    private Long postId; // 게시글 id

    private String nickname; // 댓글 작성자

    private LocalDateTime currentDateTime; // 댓글 작성 시간



}
