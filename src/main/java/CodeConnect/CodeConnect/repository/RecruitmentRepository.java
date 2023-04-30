package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findByAddressOrderByCurrentDateTimeDesc(String address); // 주소 기준 시간 내림차순 정렬

    List<Recruitment> findByAddressAndFieldInOrderByCurrentDateTimeDesc(String address, List<String> fieldList); // 주소와 관심분야가 같은 항목을 찾음

    List<Recruitment> findAllByOrderByCurrentDateTimeDesc(); // 전체 게시글 시간 내림차순 정렬

    List<Recruitment> findByTitleContainingOrContentContaining(String title, String content);
}
