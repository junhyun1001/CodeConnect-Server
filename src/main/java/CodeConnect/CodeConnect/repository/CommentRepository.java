package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByQna(Qna qna);
    List<Comment> findAllByQnaOrderByCurrentDateTimeDesc(Qna qna);
}
