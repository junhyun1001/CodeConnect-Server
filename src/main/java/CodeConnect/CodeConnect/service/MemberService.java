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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 회원가입
    public ResponseDto<?> signUp(SignUpRequestDto dto) {
        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();

        validateDuplicateMember(dto.getEmail()); // 이메일 중복 체크

        // 비밀번호 2중 체크 값이 다를 때
        if(!password.equals(passwordCheck))
            return ResponseDto.setFail("Password does not match");

        // 클라이언트에서 받아온 값으로 Member 객체 생성
        Member member = new Member(dto);
        memberRepository.save(member);

        return ResponseDto.setSuccess("SignUp Success", null);

    }

    private void validateDuplicateMember(String email) {
        Optional<Member> result = memberRepository.findById(email);
        if(result.isPresent()) {
            throw new IllegalStateException("Email already exists");
        }
    }

    // 로그인
    public ResponseDto<SignInResponseDto> signIn(SignInRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        boolean findMember = memberRepository.existsByEmailAndPassword(email, password);

        if(!findMember)
            return ResponseDto.setFail("Email and Password does not match");

        Member member = memberRepository.findById(email).get(); // Jparepository를 구현한 memberRepository의 findById() 메소드는 Optional이기 때문에 get() 메소드를 통해 값을 불러올 수 있음
        member.setPassword("");

//        String token = tokenProvider.create(email); // 토큰 생성
//        int exprTime = 3600000;

//        return ResponseDto.setSuccess("Sign In Success", new SignInResponseDto(token, exprTime, member));

        return ResponseDto.setSuccess("Sign In Success", new SignInResponseDto());

    }

    // 회원 탈퇴


    // 회원 수정

}
