package CodeConnect.CodeConnect.domain.post;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Qna")
@Table(name = "Qna")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Qna extends Post {

    @Id
    @GeneratedValue
    private Long qnaId;

}
