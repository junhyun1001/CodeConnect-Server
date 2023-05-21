package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.profile.ProfileDto;
import CodeConnect.CodeConnect.dto.member.UpdateMemberDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.QnaRepository;
import CodeConnect.CodeConnect.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;
    private final RecruitmentRepository recruitmentRepository;

    public ResponseDto<Object> showUserInfo(String email, String nickname) {
        Member findMember = memberRepository.findByEmail(email);
        if (findMember == null) {
            return ResponseDto.setFail("회원을 찾을 수 없습니다.");
        }

        String findMemberNickname = findMember.getNickname();
        if (findMemberNickname == null || findMemberNickname.isEmpty()) {
            return ResponseDto.setFail("회원을 찾을 수 없습니다.");
        }

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
// 조회한 회원 프로필에 해당 회원이 참여한 스터디
//    public ResponseDto<?> showJoinRecruitment(String email, String nickname){
//
//    }

    //조회한 회원 프로필에 해당 회원이 작성한 스터디 게시글
    public ResponseDto<Object> showUserRecruitment(String email, String nickname) {
        Member findMember = memberRepository.findByEmail(email);
        if (findMember == null) {
            return ResponseDto.setFail("회원을 찾을 수 없습니다.");
        }

        String findMemberNickname = findMember.getNickname();
        if (findMemberNickname == null || findMemberNickname.isEmpty()) {
            return ResponseDto.setFail("회원을 찾을 수 없습니다.");
        }
        if (findMemberNickname.equals(nickname)) {
            List<Recruitment> recruitmentList = recruitmentRepository.findByMember(findMember);
            return ResponseDto.setSuccess("프로필 회원 본인이 작성한 스터디 게시글 조회 성공",recruitmentList);
        }else {
            Member otherMember = memberRepository.findByNickname(nickname);
            if (otherMember != null) {
                // 다른 사용자가 작성한 Recruitment 게시글 조회
                List<Recruitment> recruitmentList = recruitmentRepository.findByMember(otherMember);
                return ResponseDto.setSuccess("다른 사용자 스터디 게시글 조회 성공", recruitmentList);
            } else {
                return ResponseDto.setFail("사용자를 찾을 수 없습니다.");
            }
        }
    }
//    //조회한 회원 프로필에 해당 회원이 작성한 Qna 게시글
    public ResponseDto<Object> showUserQna(String email, String nickname){
        Member findMember = memberRepository.findByEmail(email);
        if (findMember == null) {
            return ResponseDto.setFail("회원을 찾을 수 없습니다.");
        }

        String findMemberNickname = findMember.getNickname();
        if (findMemberNickname == null || findMemberNickname.isEmpty()) {
            return ResponseDto.setFail("회원을 찾을 수 없습니다.");
        }
        if (findMemberNickname.equals(nickname)) {
            List<Qna> qnaList = qnaRepository.findByMember(findMember);
            return ResponseDto.setSuccess("프로필 회원 본인이 작성한 Qna 게시글 조회 성공", qnaList);
        } else {
            Member otherMember = memberRepository.findByNickname(nickname);
            if (otherMember != null) {
                // 다른 사용자가 작성한 Qna 게시글 조회
                List<Qna> qnaList = qnaRepository.findByMember(otherMember);
                return ResponseDto.setSuccess("다른 사용자 Qna 게시글 조회 성공", qnaList);
            } else {
                return ResponseDto.setFail("사용자를 찾을 수 없습니다.");
            }
        }
    }
    @Transactional
    public ResponseDto<UpdateMemberDto> updateProfile(UpdateMemberDto updateMemberDto, String email) {
        Member findMember = memberRepository.findByEmail(email);
        String updatedNickname = updateMemberDto.getNickname();
        String updatedAddress = updateMemberDto.getAddress();
        List<String> updatedFieldList = updateMemberDto.getFieldList();

        // 회원 정보 업데이트
        findMember.setNickname(updatedNickname);
        findMember.setAddress(updatedAddress);
        findMember.setFieldList(updatedFieldList);
        memberRepository.save(findMember);

        // Qna 게시글 업데이트
        List<Qna> qnaList = qnaRepository.findByMember(findMember);
        for (Qna qna : qnaList) {
            qna.setNickname(updatedNickname);
        }

        // Recruitment 게시글 업데이트
        List<Recruitment> recruitmentList = recruitmentRepository.findByMember(findMember);
        for (Recruitment recruitment : recruitmentList) {
            recruitment.setNickname(updatedNickname);
            recruitment.setAddress(updatedAddress);
        }

        log.info("************************* {} 회원 정보가 수정되었습니다. *************************", findMember.getEmail());
        return ResponseDto.setSuccess("업데이트가 완료되었습니다.", updateMemberDto);
    }


}
