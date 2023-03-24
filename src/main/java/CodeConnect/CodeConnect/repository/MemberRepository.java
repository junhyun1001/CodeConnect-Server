package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByEmailAndPassword(String email, String password);

}