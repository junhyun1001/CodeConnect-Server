package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SignupValidateService {

    private final MemberRepository memberRepository;

    public boolean passwordDoubleCheck(String password, String passwordCheck) {
        return !password.equals(passwordCheck);
    }

    public boolean duplicateEmailCheck(String email) {
        Optional<Member> findMember = memberRepository.findById(email);
        return findMember.isPresent();
    }

    public boolean duplicateNicknameCheck(String nickname) {
        Optional<Member> findMember = Optional.ofNullable(memberRepository.findByNickname(nickname));
        return findMember.isPresent();
    }

    public boolean validateEmail(String email) {
        String pattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        return !Pattern.matches(pattern, email);
    }

    public boolean validatePassword(String password) {
        String pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
        return !Pattern.matches(pattern, password);
    }

    public boolean validateNickname(String nickname) {
        String pattern = "^[A-Za-z0-9가-힣]{1,18}$";
        return !Pattern.matches(pattern, nickname);
    }
}
