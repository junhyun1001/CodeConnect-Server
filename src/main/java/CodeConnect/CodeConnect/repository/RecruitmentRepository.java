package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Post;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

//    @Query("SELECT p FROM Post p WHERE p.address = :address AND p.fieldList IN :fieldList")
//    List<Post> findByAddressAndFieldList(@Param("address") String address, @Param("fieldList") List<String> fieldList);

    List<Recruitment> findByAddress(String address);

}
