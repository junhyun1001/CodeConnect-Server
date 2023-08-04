package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.SignInRequestDto;
import CodeConnect.CodeConnect.dto.member.SignInResponseDto;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
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

    @DeleteMapping("/delete")
    public ResponseDto<?> delete(@AuthenticationPrincipal String email) {
        return memberService.deleteMember(email);
    }

    @PostMapping("/logout")
    public ResponseDto<String> logout(@RequestBody String accessToken) {
        return memberService.logout(accessToken);
    }
}