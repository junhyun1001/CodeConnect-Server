package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.SignInRequestDto;
import CodeConnect.CodeConnect.dto.SignUpRequestDto;
import CodeConnect.CodeConnect.dto.UpdateRequestDto;
import CodeConnect.CodeConnect.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor // final 키워드가 붙은 핃드들을 자동으로 injection 해줌
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseDto<?> signUp(@RequestBody SignUpRequestDto signUpDto) {
        return memberService.signUp(signUpDto);
    }

    @GetMapping("/signIn")
    public ResponseDto<?> signIn(@RequestBody SignInRequestDto signInDto) {
        return memberService.signIn(signInDto);
    }

    @PutMapping("/update")
    public ResponseDto<?> update(@RequestBody UpdateRequestDto updateDto, HttpServletRequest request) {
//        String token = request.getHeader("Authorization").substring(7);
        return memberService.updateMember(updateDto);
    }

    @DeleteMapping("/delete")
    public ResponseDto<?> delete(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return memberService.deleteMember(token);
    }

}
