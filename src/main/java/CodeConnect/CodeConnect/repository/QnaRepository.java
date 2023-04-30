package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    Page<Qna> findByTitleContaining(String searchKeyword, Pageable pageable);
}
