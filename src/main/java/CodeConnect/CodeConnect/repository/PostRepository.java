package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//    @Query("SELECT p FROM Post p WHERE p.address = :address AND p.fieldList IN :fieldList")
//    List<Post> findByAddressAndFieldList(@Param("address") String address, @Param("fieldList") List<String> fieldList);

    List<Post> findByAddress(String address);


}
