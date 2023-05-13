package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.*;
import CodeConnect.CodeConnect.dto.member.*;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseDto<Member> signUp(SignUpRequestDto dto) {

        @Email
        String email = dto.getEmail();
        String nickname = dto.getNickname();
        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();
        List<String> fieldList = dto.getFieldList();

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

        // 관심분야 값 체크
        if(fieldList.size() > 2){
            return ResponseDto.setFail("관심 분야는 2개까지 선택 가능합니다.");
        }

        // 클라이언트에서 받아온 값으로 Member 객체 생성
        Member member = new Member(dto);
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(password));
        // 회원 객체 DB 저장
        Member savedMember = memberRepository.save(member);

        log.info("************************* {} 회원가입 성공 *************************", savedMember.getEmail());
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
    @Transactional(readOnly = true)
    public ResponseDto<SignInResponseDto> signIn(SignInRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        Member member = memberRepository.findByEmail(email);

        // 존재하지 않는 회원
        if (member == null)
            return ResponseDto.setFail("존재하지 않는 회원 입니다.");

        // 비밀번호가 일치하지 않을 때
        if (!passwordEncoder.matches(password, member.getPassword()))
            return ResponseDto.setFail("비밀번호가 일치하지 않습니다.");

        // 새로운 토큰 생성
        String token = tokenProvider.create(email); // 토큰 생성
        int exprTime = 3600000;

        log.info("************************* {} 로그인 성공 *************************", email);
        return ResponseDto.setSuccess("로그인 성공", new SignInResponseDto(token, exprTime, member));

    }

    // 회원 수정(프로필 이미지, 지역, 관심분야)
    public ResponseDto<Member> updateMember(UpdateMemberDto dto, String email) {

        // jwt로 해당 회원을 찾음
        Optional<Member> member = memberRepository.findById(email);
        if (member.isEmpty())
            return ResponseDto.setFail("존재하지 않는 회원 입니다");
        else {
            Member updateMember = member.get();
            updateMember.updateMember(dto);

            memberRepository.save(updateMember);

            log.info("************************* {} 회원 정보가 수정되었습니다. *************************", updateMember.getEmail());
            return ResponseDto.setSuccess("업데이트가 완료되었습니다.", updateMember);
        }
    }

    // 회원 탈퇴
    public ResponseDto<?> deleteMember(String email) {

        Optional<Member> findMember = memberRepository.findById(email);
        if (findMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }
        Member member = findMember.get();

        memberRepository.delete(member);

        log.info("************************* {} 회원이 삭제되었습니다. *************************", member.getEmail());
        return ResponseDto.setSuccess("회원이 삭제되었습니다.", null);
    }

}
