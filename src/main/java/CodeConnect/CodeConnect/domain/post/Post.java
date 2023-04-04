package CodeConnect.CodeConnect.domain.post;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 게시글 정보 클래스
 * 추상클래스로 구현하여 일반 스터디 모집 게시글과 Q&A 게시글을 구현할것
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 자식 클래스마다 각각의 테이블을 생성하면서도, 부모 클래스의 필드를 그대로 사용
@Getter
@Setter
public abstract class Post {

    @NotBlank
    String title; // 제목

    @NotBlank
    String content; // 내용

    String nickname; // 작성자 닉네임

    String email;

    LocalDateTime currentDateTime; // 작성 날짜와 시간 정보

    LocalDateTime modifiedDateTime; // 수정 날짜와 시간 정보

}