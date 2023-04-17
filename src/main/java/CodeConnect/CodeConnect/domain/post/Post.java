package CodeConnect.CodeConnect.domain.post;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 게시글 정보 클래스
 * 추상클래스로 구현하여 일반 스터디 모집 게시글과 Q&A 게시글을 구현할것
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Post {

    @NotBlank
    String title; // 제목

    @NotBlank
    String content; // 내용

    String nickname; // 작성자 닉네임

    String currentDateTime; // 작성 날짜와 시간 정보

    String modifiedDateTime; // 수정 날짜와 시간 정보

    public String changeDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

}