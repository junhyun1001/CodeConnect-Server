package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
