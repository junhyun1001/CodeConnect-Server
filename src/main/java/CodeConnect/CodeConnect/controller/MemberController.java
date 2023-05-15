package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.SignInRequestDto;
import CodeConnect.CodeConnect.dto.member.SignInResponseDto;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
import CodeConnect.CodeConnect.dto.member.UpdateMemberDto;
import CodeConnect.CodeConnect.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor // final 키워드가 붙은 핃드들을 자동으로 injection 해줌
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto<Member> signUp(@RequestBody SignUpRequestDto signUpDto) {
        return memberService.signUp(signUpDto);
    }

    @PostMapping("/login")
    public ResponseDto<SignInResponseDto> signIn(@RequestBody SignInRequestDto signInDto) {
        return memberService.signIn(signInDto);
    }

    /*
     * @AuthenticationPrincipal 은 Spring Security에서 제공하는 어노테이션으로, 현재 인증된 사용자의 정보를 주입하는데 사용된다.
     *
     * Authentication 객체에서 추출한 정보 중에서 특정 정보를 주입하도록 지정할 수 있다.
     *
     * 따라서 현재 인증된 사용자의 이메일 주소를 'email' 파라미터에 주입하는데 사용된다.
     */

    @PutMapping("/update")
    public ResponseDto<UpdateMemberDto> update(@RequestBody UpdateMemberDto updateDto, @AuthenticationPrincipal String email) {
        return memberService.updateMember(updateDto, email);
    }

    @DeleteMapping("/delete")
    public ResponseDto<?> delete(@AuthenticationPrincipal String email) {
        return memberService.deleteMember(email);
    }

}