package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.*;
import CodeConnect.CodeConnect.dto.member.SignInRequestDto;
import CodeConnect.CodeConnect.dto.member.SignInResponseDto;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
import CodeConnect.CodeConnect.dto.member.UpdateRequestDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseDto<?> signUp(SignUpRequestDto dto) {

        @Email
        String email = dto.getEmail();
        String nickname = dto.getNickname();
        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();
//        Field field = dto.getField();

        // 이메일 중복 체크
        if (duplicateEmailCheck(email))
            return ResponseDto.setFail("이미 존재하는 이메일 입니다.");

        // 닉네임 중복 체크
        if (duplicateNicknameCheck(nickname))
            return ResponseDto.setFail("이미 존재하는 닉네임 입니다.");

        // 비밀번호 2중 체크 값이 다를 때
        if (passwordDoubleCheck(password, passwordCheck))
            return ResponseDto.setFail("비밀번호가 일치하지 않습니다.");

        // 이메일 패턴 체크
        if (validateEmail(email))
            return ResponseDto.setFail("올바른 이메일 형식이 아닙니다.");

        // 비밀번호 패턴 체크
        if (validatePassword(password))
            return ResponseDto.setFail("비밀번호는 영문, 숫자, 특수문자 8~10자리만 가능합니다.");

        // 닉네임 패턴 체크
        if (validateNickname(nickname))
            return ResponseDto.setFail("닉네임은 한글, 영문, 숫자 18자리 이하만 사용 가능합니다.");


        // 클라이언트에서 받아온 값으로 Member 객체 생성
        Member member = new Member(dto);
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(password));
        // 관심분야 설정
//        member.setField(dto.getField());
        // 회원 객체 DB 저장
        Member savedMember = memberRepository.save(member);

        return ResponseDto.setSuccess("회원가입 성공", savedMember);
    }

    private static boolean passwordDoubleCheck(String password, String passwordCheck) {
        return !password.equals(passwordCheck);
    }

    private boolean duplicateEmailCheck(String email) {
        Optional<Member> findMember = memberRepository.findById(email);
        return findMember.isPresent();
    }

    private boolean duplicateNicknameCheck(String nickname) {
        Optional<Member> findMember = Optional.ofNullable(memberRepository.findByNickname(nickname));
        return findMember.isPresent();
    }

    private static boolean validateEmail(String email) {
        String pattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        return !Pattern.matches(pattern, email);
    }

    private static boolean validatePassword(String password) {
        String pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
        return !Pattern.matches(pattern, password);
    }

    private static boolean validateNickname(String nickname) {
        String pattern = "^[A-Za-z0-9가-힣]{1,18}$";
        return !Pattern.matches(pattern, nickname);
    }


    // 로그인
    public ResponseDto<?> signIn(SignInRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        Member member = memberRepository.findByEmail(email);

        // 존재하지 않는 회원 이메일 일 때
        if (member == null)
            return ResponseDto.setFail("존재하지 않는 이메일 입니다.");

        // 비밀번호가 일치하지 않을 때
        if (!passwordEncoder.matches(password, member.getPassword()))
            return ResponseDto.setFail("비밀번호가 일치하지 않습니다.");

        String token = tokenProvider.create(email, member.getNickname(), member.getFieldList()); // 토큰 생성
        int exprTime = 3600000;

        return ResponseDto.setSuccess("로그인 성공", new SignInResponseDto(token, exprTime, member));

    }

    // 회원 수정(프로필 이미지, 지역, 관심분야)
    // 토큰을 인증하고 값을 변경해야되는데 어떻게 할지 모르겠음
    public ResponseDto<?> updateMember(UpdateRequestDto dto) {

//        Claims claims = tokenProvider.validate(token);

        Optional<Member> member = memberRepository.findById(dto.getEmail());
        if (member.isEmpty())
            return ResponseDto.setFail("존재하지 않는 회원 입니다");
        Member updateMember = member.get();

        // 지역과 관심분야 중 하나만 수정할 때, 모두 수정 할 때 어떻게 처리?
        updateMember.setFieldList(dto.getFieldList());
        updateMember.setAddress(dto.getAddress());

        memberRepository.save(updateMember);


        return ResponseDto.setSuccess("업데이트가 완료되었습니다.", updateMember);
    }


    // 회원 탈퇴
    public ResponseDto<?> deleteMember(String token) {

        String validateEmail = tokenProvider.validate(token).getSubject(); // 토큰의 email claims 값을 가져옴

        Optional<Member> member = memberRepository.findById(validateEmail);
        if (member.isEmpty())
            return ResponseDto.setFail("존재하지 않는 회원 입니다");
        Member findMember = member.get();

        memberRepository.delete(findMember);

        return ResponseDto.setSuccess("회원이 삭제되었습니다.", null);
    }

}
