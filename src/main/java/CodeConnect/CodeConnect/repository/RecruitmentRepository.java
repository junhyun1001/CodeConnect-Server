package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findByAddressOrderByCurrentDateTimeDesc(String address); // 주소 기준 시간 내림차순 정렬

    List<Recruitment> findByAddressAndFieldInOrderByCurrentDateTimeDesc(String address, List<String> fieldList); // 주소와 관심분야가 같은 항목을 찾음

    List<Recruitment> findAllByOrderByCurrentDateTimeDesc(); // 전체 게시글 시간 내림차순 정렬

    List<Recruitment> findByTitleContainingOrContentContaining(String title, String content); // 제목, 내용으로 일치하는 항목 찾기

    List<Recruitment> findByAddressAndTitleContainingOrAddressAndContentContaining(String address1, String keyword1, String address2, String keyword2); // 주소, 키워드로 찾기

    List<Recruitment> findByMemberOrderByCurrentDateTimeDesc(Member findMember);
    List<Recruitment> findByCurrentParticipantMemberListContainingOrderByCurrentDateTimeDesc(String email);
}

