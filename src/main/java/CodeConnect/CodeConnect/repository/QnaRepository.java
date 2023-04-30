package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findAllByOrderByCurrentDateTimeDesc();
    List<Qna> findByContentContainingOrderByCurrentDateTimeDesc(String text);
}
