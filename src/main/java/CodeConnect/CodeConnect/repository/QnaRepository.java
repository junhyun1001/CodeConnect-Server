package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findAllByOrderByCurrentDateTimeDesc();

    List<Qna> findByTitleContainingOrContentContainingOrderByCurrentDateTimeDesc(String title, String content);

    List<Qna> findByMemberOrderByCurrentDateTimeDesc(Member findMember);

    List<Qna> findTop10ByOrderByLikeCountDesc();

}
