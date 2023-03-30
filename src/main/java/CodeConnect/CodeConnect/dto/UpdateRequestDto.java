package CodeConnect.CodeConnect.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRequestDto {

    private String email;

    private String nickname;

    private String state;

    private String city;

//    private String street;

    private List<String> fieldList;
}
