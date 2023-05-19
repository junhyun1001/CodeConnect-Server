package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.ProfileDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;

    public ResponseDto<List<ProfileDto>> showProfile(String email){
        Optional<Member> optionalMember = Optional.ofNullable(memberRepository.findByEmail(email));
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            ProfileDto profileDto = new ProfileDto(member);
            List<ProfileDto> profileDtoList = Arrays.asList(profileDto);
            return ResponseDto.setSuccess("프로필 조회 성공", profileDtoList);
        } else {
            return ResponseDto.setFail("회원을 찾을 수 없습니다.");
        }
    }

    public ResponseDto<Object> showProfile(String email, String nickname) {
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        if (findMemberNickname.equals(nickname)) {
            ProfileDto profileDto = new ProfileDto(findMember);
            profileDto.setRole(Role.HOST);

            return ResponseDto.setSuccess("본인 프로필 조회 성공", profileDto);
        } else {
            Member otherMember = memberRepository.findByNickname(nickname);
            if (otherMember != null) {
                ProfileDto otherProfileDto = new ProfileDto(otherMember);
                otherProfileDto.setRole(Role.GUEST);

                return ResponseDto.setSuccess("다른 사용자 프로필 조회 성공", otherProfileDto);
            } else {
                return ResponseDto.setFail("사용자를 찾을 수 없습니다.");
            }
        }
    }
}