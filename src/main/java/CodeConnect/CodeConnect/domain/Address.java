package CodeConnect.CodeConnect.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String state; // 경기도, 서울특별시
    private String city; // 파주시, 성북구
    private String street; // 문산읍, 성북동

}
