package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.SignInRequestDto;
import CodeConnect.CodeConnect.dto.member.SignInResponseDto;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
import CodeConnect.CodeConnect.dto.member.EditMemberDto;
import CodeConnect.CodeConnect.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor // final 키워드가 붙은 핃드들을 자동으로 injection 해줌
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseDto<Member> signUp(@RequestBody SignUpRequestDto signUpDto) {
        return memberService.signUp(signUpDto);
    }

    @PostMapping("/signIn")
    public ResponseDto<SignInResponseDto> signIn(@RequestBody SignInRequestDto signInDto) {
        return memberService.signIn(signInDto);
    }

    @PutMapping("/edit")
    public ResponseDto<Member> edit(@RequestBody EditMemberDto updateDto, @AuthenticationPrincipal String email) {
        return memberService.editMember(updateDto, email);
    }

    @DeleteMapping("/delete")
    public ResponseDto<?> delete(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return memberService.deleteMember(token);
    }

}
