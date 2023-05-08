package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.UpdateRecruitmentDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecruitmentService {

    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;

    // 게시글 쓰기
    public ResponseDto<Recruitment> createPost(CreateRecruitmentDto dto, String email) {

        Member findMember = memberRepository.findByEmail(email); // 토큰에 포함된 email 값으로 회원 조회
        String nickname = findMember.getNickname();
        String address = findMember.getAddress();

        Recruitment recruitment = new Recruitment(dto, nickname, address); // 받아온 게시글 데이터로 게시글 생성

        Recruitment savedRecruitment = recruitmentRepository.save(recruitment); // 게시글 영속화

        findMember.setRecruitment(savedRecruitment); // 연관관계 메소드를 사용하여 회원 엔티티에 모집 게시글 엔티티를 설정해줌

        log.info("{}회원 {}번 게시글 저장", email, savedRecruitment.getRecruitmentId());
        return ResponseDto.setSuccess("게시글이 저장되었습니다.", savedRecruitment);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public ResponseDto<List<Recruitment>> getAllPosts() {

        List<Recruitment> findAllRecruitments = recruitmentRepository.findAllByOrderByCurrentDateTimeDesc();

        return ResponseDto.setSuccess("전체 리스트 조회", findAllRecruitments);

    }

    // 주소, 관심분야 기준 게시글 조회 또는 주소 기준 검색
    @Transactional(readOnly = true)
    public ResponseDto<List<Recruitment>> getPostsByAddressAndFieldOrSearchByAddress(String email, String searchAddress) {
        // 글을 작성한 회원의 정보
        Member findMember = memberRepository.findByEmail(email);
        String address = findMember.getAddress();
        List<String> fieldList = findMember.getFieldList();

        List<Recruitment> recruitmentList;

        // 주소 검색이 있을 때와 없을 때
        if (searchAddress != null && !searchAddress.isEmpty()) {
            recruitmentList = recruitmentRepository.findByAddressOrderByCurrentDateTimeDesc(searchAddress);
        } else {
            recruitmentList = recruitmentRepository.findByAddressAndFieldInOrderByCurrentDateTimeDesc(address, fieldList);
        }
        return ResponseDto.setSuccess("글 불러오기 성공", recruitmentList);

    }

    // 게시글 단일 조회
    @Transactional(readOnly = true)
    public ResponseDto<Map<Role, Object>> getPost(String email, Long id) {

        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        Recruitment recruitment = validateExistPost(id);

        // 회원 검증 후 내 게시글이면 HOST, 아니면 GUEST
        // 조회 할 시점에 참여된 회원인지 아닌지 판별 할 수 있어야 함
        Map<Role, Object> recruitmentMap = new HashMap<>();
        if (validateMember(email, recruitment)) { // false 값이 반환 될 때
            boolean participantExist = isParticipantExist(recruitment, email);
            recruitmentMap.put(Role.GUEST, recruitment);
            recruitmentMap.put(Role.PARTICIPATION, participantExist);
            log.info("************************* GUEST로 게시글 조회 *************************");
            return ResponseDto.setSuccess("GUEST 게시글 조회", recruitmentMap);
        } else {
            recruitmentMap.put(Role.HOST, recruitment);
            log.info("************************* HOST로 게시글 조회 *************************");
            return ResponseDto.setSuccess("HOST 게시글 조회", recruitmentMap);
        }

    }

    // 게시글 내용, 주소 동시 검색
    @Transactional(readOnly = true)
    public ResponseDto<List<Recruitment>> getContentBySearch(String keyword, String searchAddress) {

        if (searchAddress == null || searchAddress.isEmpty()) {
            // 주소 검색이 없고 검색어가 있는 경우
            List<Recruitment> byTitleContainingOrContentContaining = recruitmentRepository.findByTitleContainingOrContentContaining(keyword, keyword);
            return ResponseDto.setSuccess("주소x 검색o", byTitleContainingOrContentContaining);
        } else {
            // 주소 검색이 있는 경우
            if (keyword == null || keyword.isEmpty()) {
                // 검색어가 없는 경우
                List<Recruitment> byAddressOrderByCurrentDateTimeDesc = recruitmentRepository.findByAddressOrderByCurrentDateTimeDesc(searchAddress);
                return ResponseDto.setSuccess("주소o 검색x", byAddressOrderByCurrentDateTimeDesc);
            } else {
                // 검색어가 있는 경우
                List<Recruitment> byAddressAndTitleContainingOrContentContaining = recruitmentRepository.findByAddressAndTitleContainingOrAddressAndContentContaining(searchAddress, keyword, searchAddress, keyword);
                return ResponseDto.setSuccess("주소o 검색o", byAddressAndTitleContainingOrContentContaining);
            }
        }

//        List<Recruitment> byTitleContainingOrContentContaining = recruitmentRepository.findByTitleContainingOrContentContaining(keyword, keyword);

//        return ResponseDto.setSuccess("주소와 검색어 리스트 반환", byTitleContainingOrContentContaining);

    }

    // 게시글 수정 -> 게시글 id를 받아서 해당 게시글을 수정함(리스트로 여러개 있기 때문)
    // 토큰 값이랑 현재 회원이랑 같은지 판단 해야됨
    public ResponseDto<Recruitment> editPost(UpdateRecruitmentDto dto, Long id, String email) {

        // 해당 게시글을 id로 조회함
        Recruitment recruitment = validateExistPost(id);

        // 회원 검증
        if (validateMember(email, recruitment))
            return ResponseDto.setFail("해당 게시글의 수정 권한이 없습니다.");

        // 해당 게시글을 업데이트 시킴
        recruitment.updatePost(dto);

        recruitmentRepository.save(recruitment);

        log.info("************************* {}의 {}번 게시글이 수정되었습니다. *************************", email, recruitment.getRecruitmentId());
        return ResponseDto.setSuccess("게시글이 수정되었습니다.", recruitment);
    }

    // 게시글 삭제
    public ResponseDto<String> deletePost(String email, Long id) {

        Recruitment recruitment = validateExistPost(id);

        // 회원 검증
        if (validateMember(email, recruitment))
            return ResponseDto.setFail("해당 게시글의 삭제 권한이 없습니다.");

        recruitmentRepository.delete(recruitment);

        log.info("************************* {}의 {}번 게시글이 삭제되었습니다. *************************", email, recruitment.getRecruitmentId());
        return ResponseDto.setSuccess("게시글이 삭제되었습니다.", null);
    }

    // 스터디 참여 여부 처리
    public ResponseDto<Integer> participate(String email, Long id, Boolean isParticipating) {

        if (email.isBlank()) {
            return ResponseDto.setFail("email이 빈칸 입니다.");
        }

        Recruitment recruitment = validateExistPost(id);

        if (isParticipating)
            return addMemberInPost(recruitment, email);
        else
            return subtractMemberInPost(recruitment, email);
    }

    // 참여 회원 추가
    public ResponseDto<Integer> addMemberInPost(Recruitment recruitment, String email) {

        log.info("참여 회원 추가 email:{}", email);

        int count = recruitment.getCount();
        int currentCount = recruitment.getCurrentCount();

        if (count == currentCount) {
            return ResponseDto.setSuccess("더 이상 참여할 수 없습니다.", -1);
        }

        if (isParticipantExist(recruitment, email)) {
            return ResponseDto.setFail("이미 참여하였습니다.");
        } else {
            ++currentCount;
            updateMemberInPost(recruitment, email, currentCount, true);
            return ResponseDto.setSuccess("인원이 추가되었습니다.", currentCount);
        }
    }

    // 참여 회원 삭제
    public ResponseDto<Integer> subtractMemberInPost(Recruitment recruitment, String email) {

        int currentCount = recruitment.getCurrentCount();

        if (isParticipantExist(recruitment, email)) {
            --currentCount;
            updateMemberInPost(recruitment, email, currentCount, false);
            return ResponseDto.setSuccess("취소되었습니다.", currentCount);
        } else return ResponseDto.setFail("이미 취소하였거나 참여하지 않았습니다.");
    }

    // 참여 회원 정보 업데이트
    public void updateMemberInPost(Recruitment recruitment, String email, int currentCount, Boolean bool) {
        recruitment.setCurrentCount(currentCount);
        if (bool)
            recruitment.getCurrentParticipantMemberList().add(email);
        else
            recruitment.getCurrentParticipantMemberList().remove(email);
        recruitmentRepository.save(recruitment);
    }

    // 스터디 참여 여부 확인(email)
    public boolean isParticipantExist(Recruitment recruitment, String email) {
        return recruitment.getCurrentParticipantMemberList().stream().anyMatch(participant -> participant.equals(email));
    }

    // 해당 게시글 존재 여부 확인
    public Recruitment validateExistPost(Long id) {
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        return optionalRecruitment.orElse(null);
    }

    // 게시글 수정, 삭제 하려는 회원이 현재 로그인 된 회원의 email과 게시글의 email이 같은지 확인함
    private boolean validateMember(String email, Recruitment recruitment) {
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String memberEmail = findMember.getEmail();

        // 게시글
        String recruitmentEmail = recruitment.getMember().getEmail();

        return !memberEmail.equals(recruitmentEmail); // 같지 않으면 false를 반환
    }

}