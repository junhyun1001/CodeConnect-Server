package CodeConnect.CodeConnect.domain.post;

import CodeConnect.CodeConnect.domain.Member;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 게시글 정보 클래스
 * 추상클래스로 구현하여 일반 스터디 모집 게시글과 Q&A 게시글을 구현할것
 */

public abstract class Post {

    @Id
    @GeneratedValue
    private Long postId; // 게시글 id

    private Member member; // 작성자 정보

    private String title; // 제목

    private String content; // 내용

    private final LocalDateTime currentDateTime = LocalDateTime.now(); // 작성 날짜와 시간 정보
    
    private LocalDateTime modifiedDateTime; // 수정 날짜와 시간 정보

    private Comment comment; // 댓글

}