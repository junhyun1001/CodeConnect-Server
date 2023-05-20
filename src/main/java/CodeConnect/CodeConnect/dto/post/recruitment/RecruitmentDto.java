package CodeConnect.CodeConnect.dto.post.recruitment;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import lombok.Getter;

@Getter
public class RecruitmentDto {

    private final Long recruitmentId; // 모집 게시글 id

    private final String title; // 제목

    private final String content; // 내용

    private final String nickname; // 작성자 닉네임

    private final String currentDateTime; // 작성 날짜와 시간 정보

    private final String modifiedDateTime; // 수정 날짜와 시간 정보

    private final String address; // 작성자 주소

    private final int count; // 해당 게시글의 제한 인원수

    private final int currentCount; // 해당 게시글의 현재 참가인원

    private final String field; // 관심분야

    public RecruitmentDto(Recruitment recruitment) {
        this.recruitmentId = recruitment.getRecruitmentId();
        this.title = recruitment.getTitle();
        this.content = recruitment.getContent();
        this.nickname = recruitment.getNickname();
        this.currentDateTime = recruitment.getCurrentDateTime();
        this.modifiedDateTime = recruitment.getModifiedDateTime();
        this.address = recruitment.getAddress();
        this.count = recruitment.getCount();
        this.currentCount = recruitment.getCurrentCount();
        this.field = recruitment.getField();
    }

}
