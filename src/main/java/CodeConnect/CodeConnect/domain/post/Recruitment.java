package CodeConnect.CodeConnect.domain.post;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Recruitment")
@Table(name = "Recruitment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recruitment extends Post {

    @Id
    @GeneratedValue
    private Long recruitmentId; // 모집 게시글 id

    private int count; // 인원수

    /**
     * 여기에 지금 address 필드가 없어서 RecruitmentRepository에서 findByAddress를 못하고있음
     */
}
