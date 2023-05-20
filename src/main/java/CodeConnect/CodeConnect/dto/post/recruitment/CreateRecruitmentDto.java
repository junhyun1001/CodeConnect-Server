package CodeConnect.CodeConnect.dto.post.recruitment;

import lombok.Getter;

@Getter
public class CreateRecruitmentDto {

    private String title; // 제목

    private String content; // 내용

    private int count; // 인원수

    private String field; // 관심분야

}
