package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.SignInRequestDto;
import CodeConnect.CodeConnect.dto.member.SignInResponseDto;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SignupValidateService signupValidateService;

    @Value("${default.image}")
    private String imagePath; // 외부 저장소에 있는 이미지 파일 경로

    // 회원가입
    public ResponseDto<Member> signUp(SignUpRequestDto dto) {

        @Email
        String email = dto.getEmail();
        String nickname = dto.getNickname();
        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();
        String address = dto.getAddress();
        List<String> fieldList = dto.getFieldList();

        // 이메일 중복 체크
        if (signupValidateService.duplicateEmailCheck(email))
            return ResponseDto.setFail("이미 존재하는 이메일 입니다.");

        // 닉네임 중복 체크
        if (signupValidateService.duplicateNicknameCheck(nickname))
            return ResponseDto.setFail("이미 존재하는 닉네임 입니다.");

        // 비밀번호 2중 체크 값이 다를 때
        if (signupValidateService.passwordDoubleCheck(password, passwordCheck))
            return ResponseDto.setFail("비밀번호가 일치하지 않습니다.");

        // 이메일 패턴 체크
        if (signupValidateService.validateEmail(email))
            return ResponseDto.setFail("올바른 이메일 형식이 아닙니다.");

        // 비밀번호 패턴 체크
        if (signupValidateService.validatePassword(password))
            return ResponseDto.setFail("비밀번호는 영문, 숫자, 특수문자 8~10자리만 가능합니다.");

        // 닉네임 패턴 체크
        if (signupValidateService.validateNickname(nickname))
            return ResponseDto.setFail("닉네임은 한글, 영문, 숫자 18자리 이하만 사용 가능합니다.");

        // 관심분야 값 체크
        if (fieldList.size() > 2) {
            return ResponseDto.setFail("관심 분야는 2개까지 선택 가능합니다.");
        }

        if (address == null || address.isEmpty()) {
            return ResponseDto.setFail("주소를 입력해 주세요");
        }

        // 클라이언트에서 받아온 값으로 Member 객체 생성
        Member member = new Member(dto);
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(password));
        // 프로필 이미지 기본값 설정
        member.setProfileImagePath(imagePath);
        // 회원 객체 DB 저장
        memberRepository.save(member);

        log.info("************************* {} 회원가입 성공 *************************", member.getEmail());
        return ResponseDto.setSuccess("회원가입 성공", null);

    }

    // 로그인
    @Transactional(readOnly = true)
    public ResponseDto<SignInResponseDto> signIn(SignInRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        Member member = validateExistMember(email);
        if (member == null) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        // 비밀번호가 일치하지 않을 때
        if (!passwordEncoder.matches(password, member.getPassword()))
            return ResponseDto.setFail("비밀번호가 일치하지 않습니다.");

        // 새로운 토큰 생성
        String token = tokenProvider.create(email); // 토큰 생성
        int exprTime = 3600000;

        log.info("************************* {} 로그인 성공 *************************", email);
        return ResponseDto.setSuccess("로그인 성공", new SignInResponseDto(token, exprTime, member));

    }

    // 회원 탈퇴
    public ResponseDto<?> deleteMember(String email) {

        Member member = validateExistMember(email);
        if (member == null) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        memberRepository.delete(member);

        log.info("************************* {} 회원이 삭제되었습니다. *************************", member.getEmail());
        return ResponseDto.setSuccess("회원이 삭제되었습니다.", null);
    }

    // 해당 회원 존재 여부 확인
    public Member validateExistMember(String email) {
        Optional<Member> optionalMember = memberRepository.findById(email);
        return optionalMember.orElse(null);
    }

}
