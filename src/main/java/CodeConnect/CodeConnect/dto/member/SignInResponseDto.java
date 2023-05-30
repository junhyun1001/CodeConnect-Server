package CodeConnect.CodeConnect.dto.member;

import CodeConnect.CodeConnect.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 서버에서 클라이언트로 요청에 대한 응답을 반환하는 DTO
 */

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
public class SignInResponseDto {

    private String token; // jwt 토근

    private int exprTime; // 토큰 만료기한

    private String address; // 회원 주소

    private String nickname; // 회원 닉네임

    private List<String> fieldList; // 회원 관심분야

    public SignInResponseDto(String token, int exprTime, Member member) {
        this.token = token;
        this.exprTime = exprTime;
        this.address = member.getAddress();
        this.nickname = member.getNickname();
        this.fieldList = member.getFieldList();
    }



}
