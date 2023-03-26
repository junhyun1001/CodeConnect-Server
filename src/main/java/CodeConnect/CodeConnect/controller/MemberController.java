package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.SignInRequestDto;
import CodeConnect.CodeConnect.dto.SignInResponseDto;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.SignUpRequestDto;
import CodeConnect.CodeConnect.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor // final 키워드가 붙은 핃드들을 자동으로 injection 해줌
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseDto<?> signUp(@RequestBody SignUpRequestDto requestDto) {
        return memberService.signUp(requestDto);
    }

    @PostMapping("/signIn")
    public ResponseDto<?> signIn(@RequestBody SignInRequestDto requestDto) {
        return memberService.signIn(requestDto);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseDto<?> delete(@PathVariable String email) {
        return memberService.deleteMember(email);
    }

}
