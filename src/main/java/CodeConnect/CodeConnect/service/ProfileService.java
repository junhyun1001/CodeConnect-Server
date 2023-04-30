package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.member.ProfileDto;
import CodeConnect.CodeConnect.dto.member.SignUpRequestDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
}
