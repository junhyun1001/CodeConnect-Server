package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    Recruitment findByNickname(String email);

    List<Recruitment> findByAddress(String address);

    List<Recruitment> findByAddressAndField(String address, String field);



}