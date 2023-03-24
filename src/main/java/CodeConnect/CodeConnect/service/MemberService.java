package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.SignInRequestDto;
import CodeConnect.CodeConnect.dto.SignInResponseDto;
import CodeConnect.CodeConnect.dto.SignUpRequestDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 회원가입 서비스
    public ResponseDto<?> signUp(SignUpRequestDto dto) {
        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();

        // 비밀번호 2중 체크 값이 다를 때
        if(!password.equals(passwordCheck))
            return ResponseDto.setFail("password does not matched");

        // 클라이언트에서 받아온 값으로 Member 객체 생성
        Member member = new Member(dto);
        memberRepository.save(member);

        return ResponseDto.setSuccess("SignUp Success", null);

    }

    // 로그인 서비스
    public ResponseDto<SignInResponseDto> signIn(SignInRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        boolean findMember = memberRepository.existsByEmailAndPassword(email, password);

        if(!findMember)
            return ResponseDto.setFail("Information does not match");

        Member member = memberRepository.findById(email).get(); // Jparepository를 구현한 memberRepository의 findById() 메소드는 Optional이기 때문에 get() 메소드를 통해 값을 불러올 수 있음
        member.setPassword("");

//        String token = tokenProvider.create(email); // 토큰 생성
//        int exprTime = 3600000;

//        return ResponseDto.setSuccess("Sign In Success", new SignInResponseDto(token, exprTime, member));

        return ResponseDto.setSuccess("Sign In Success", new SignInResponseDto());

    }

}
