package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.converter.FieldConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 정보 클래스
 * 추상클래스로 구현하여 일반 스터디 모집 게시글과 Q&A 게시글을 구현할것
 */

@Entity(name = "Post")
@Table(name = "Post")
@Getter
@Setter
public abstract class Post {

    @Id
    @GeneratedValue
    private Long postId; // 게시글 id

    private String title; // 제목

    private int count; // 인원수

    private String content; // 내용

    private String writer; // 작성자

    private LocalDateTime currentDateTime; // 작성 날짜와 시간 정보
    
    private LocalDateTime modifiedDateTime; // 수정 날짜와 시간 정보

    private String address; // 위치 설정

    @Convert(converter = FieldConverter.class)
    private List<String> fieldList;

}

