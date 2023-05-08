package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Cocomment;
import CodeConnect.CodeConnect.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocommentRepository extends JpaRepository<Cocomment, Long> {
    List<Cocomment> findAllByCommentOrderByCurrentDateTimeDesc(Comment comment);
}
