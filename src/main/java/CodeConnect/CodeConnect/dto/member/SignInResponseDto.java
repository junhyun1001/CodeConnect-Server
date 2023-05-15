package CodeConnect.CodeConnect.dto.member;

import CodeConnect.CodeConnect.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Member member;

}
