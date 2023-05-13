package CodeConnect.CodeConnect.dto.post.recruitment;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import lombok.Getter;

@Getter
public class RecruitmentDto {

    private final Long recruitmentId; // 모집 게시글 id

    private final String address; // 작성자 주소

    private final int count; // 해당 게시글의 제한 인원수

    private final int currentCount; // 해당 게시글의 현재 참가인원

    private final String field; // 관심분야

    public RecruitmentDto(Recruitment recruitment) {
        this.recruitmentId = recruitment.getRecruitmentId();
        this.address = recruitment.getAddress();
        this.count = recruitment.getCount();
        this.currentCount = recruitment.getCurrentCount();
        this.field = recruitment.getField();
    }

}
