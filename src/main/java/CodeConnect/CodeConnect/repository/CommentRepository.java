package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByQna(Qna qna);
}
