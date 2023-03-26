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

        String email = dto.getEmail();
        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();

        // 이메일 중복 체크
        Optional<Member> findMember = memberRepository.findById(email);
        if(findMember.isPresent()) {
            return ResponseDto.setFail("이미 존재하는 이메일 입니다.");
        }

        // 비밀번호 2중 체크 값이 다를 때
        if(!password.equals(passwordCheck))
            return ResponseDto.setFail("비밀번호가 일치하지 않습니다.");

        // 클라이언트에서 받아온 값으로 Member 객체 생성
        Member member = new Member(dto);
        Member savedMember = memberRepository.save(member);

        return ResponseDto.setSuccess("회원가입 성공", savedMember);
    }

    // 로그인
    public ResponseDto<?> signIn(SignInRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        boolean findMember = memberRepository.existsByEmailAndPassword(email, password);

        if(!findMember)
            return ResponseDto.setFail("이메일과 비밀번호가 일치하지 않습니다.");

        Member member = memberRepository.findById(email).get(); // Jparepository를 구현한 memberRepository의 findById() 메소드는 Optional이기 때문에 get() 메소드를 통해 값을 불러올 수 있음
        String token = tokenProvider.create(email); // 토큰 생성
        int exprTime = 3600000;

        return ResponseDto.setSuccess("로그인 성공", new SignInResponseDto(token, exprTime, member));

//        return ResponseDto.setSuccess("Sign In Success", member);

    }

    // 회원 탈퇴
    // jwt로 클라이언트에서 가지고 있는 토큰과 서버에서 가지고 있는 토큰이 같으면 회원 탈퇴를 시킴
    public ResponseDto<?> deleteMember(String email) {
        if(email == null)
            throw new IllegalArgumentException("이메일은 필수값 입니다.");

        Optional<Member> findMember = memberRepository.findById(email);
        if(findMember.isEmpty())
            throw new IllegalStateException("이메일이 존재하지 않습니다.");

        Member member = findMember.get();
        memberRepository.delete(member);

        return ResponseDto.setSuccess("회원 삭제.", member);
    }

    // 회원 수정(프로필 이미지, 지역, 관심분야)



}
