package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findAllByOrderByCurrentDateTimeDesc();
    List<Qna> findByTitleContainingOrContentContainingOrderByCurrentDateTimeDesc(String title, String content);
}
