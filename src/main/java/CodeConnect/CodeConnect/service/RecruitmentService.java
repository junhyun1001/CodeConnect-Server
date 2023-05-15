package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.converter.EntityToDto;
import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.RecruitmentDto;
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

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ChatRoomService chatRoomService;

    // 게시글 쓰기
    public ResponseDto<RecruitmentDto> createPost(CreateRecruitmentDto dto, String email) {

        Member member = memberService.validateExistMember(email);
        String nickname = member.getNickname();
        String address = member.getAddress();

        Recruitment recruitment = new Recruitment(dto, nickname, address); // 받아온 게시글 데이터로 게시글 생성

        recruitmentRepository.save(recruitment); // 게시글 영속화

        member.setRecruitment(recruitment); // 연관관계 메소드를 사용하여 회원 엔티티에 모집 게시글 엔티티를 설정해줌

        log.info("************************* {}회원 {}번 모집게시글 저장 *************************", email, recruitment.getRecruitmentId());
        return ResponseDto.setSuccess("게시글이 저장되었습니다.", new RecruitmentDto(recruitment));

    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public ResponseDto<List<RecruitmentDto>> getAllPosts() {

        List<Recruitment> findAllRecruitments = recruitmentRepository.findAllByOrderByCurrentDateTimeDesc();

        List<RecruitmentDto> recruitmentDtoList = EntityToDto.mapListToDto(findAllRecruitments, RecruitmentDto::new);

        return ResponseDto.setSuccess("전체 리스트 조회", recruitmentDtoList);

    }

    // 주소, 관심분야 기준 게시글 조회 또는 주소 기준 검색
    @Transactional(readOnly = true)
    public ResponseDto<List<RecruitmentDto>> getPostsByAddressAndFieldOrSearchByAddress(String email, String searchAddress) {
        // 글을 작성한 회원의 정보
        Member member = memberService.validateExistMember(email);
        String address = member.getAddress();
        List<String> fieldList = member.getFieldList();

        List<Recruitment> recruitmentList;

        // 주소 검색이 있을 때와 없을 때
        if (searchAddress != null) {
            recruitmentList = recruitmentRepository.findByAddressOrderByCurrentDateTimeDesc(searchAddress);
        } else {
            recruitmentList = recruitmentRepository.findByAddressAndFieldInOrderByCurrentDateTimeDesc(address, fieldList);
        }

        List<RecruitmentDto> recruitmentDtoList = EntityToDto.mapListToDto(recruitmentList, RecruitmentDto::new);

        return ResponseDto.setSuccess("글 불러오기 성공", recruitmentDtoList);

    }

    // 게시글 단일 조회
    @Transactional(readOnly = true)
    public ResponseDto<Map<Role, Object>> getPost(String email, Long id) {

        memberService.validateExistMember(email);

        Recruitment recruitment = validateExistPost(id);

        // 회원 검증 후 내 게시글이면 HOST, 아니면 GUEST
        // 조회 할 시점에 참여된 회원인지 아닌지 판별 할 수 있어야 함
        Map<Role, Object> recruitmentMap = new HashMap<>();
        if (validateAuthorizedMember(email, recruitment)) { // false 값이 반환 될 때
            boolean participantExist = isParticipantExist(recruitment, email);
            recruitmentMap.put(Role.GUEST, recruitment);
            recruitmentMap.put(Role.PARTICIPATION, participantExist);
            log.info("************************* GUEST로 {}번 모집게시글 조회 *************************", recruitment.getRecruitmentId());
            return ResponseDto.setSuccess("GUEST 게시글 조회", recruitmentMap);
        } else {
            recruitmentMap.put(Role.HOST, recruitment);
            log.info("************************* HOST로 {}번 모집게시글 조회 *************************", recruitment.getRecruitmentId());
            return ResponseDto.setSuccess("HOST 게시글 조회", recruitmentMap);
        }

    }

    // 게시글 내용, 주소 동시 검색
    @Transactional(readOnly = true)
    public ResponseDto<List<RecruitmentDto>> getContentBySearch(String keyword, String searchAddress) {

        List<Recruitment> searchList;

        if (searchAddress == null || searchAddress.isEmpty()) {
            // 주소 검색이 없고 검색어가 있는 경우
            searchList = recruitmentRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        } else {
            // 주소 검색이 있는 경우
            if (keyword == null || keyword.isEmpty()) {
                // 검색어가 없는 경우
                searchList = recruitmentRepository.findByAddressOrderByCurrentDateTimeDesc(searchAddress);
            } else {
                // 검색어가 있는 경우
                searchList = recruitmentRepository.findByAddressAndTitleContainingOrAddressAndContentContaining(searchAddress, keyword, searchAddress, keyword);
            }
        }

        List<RecruitmentDto> recruitmentDtoList = EntityToDto.mapListToDto(searchList, RecruitmentDto::new);

        String successMessage = (searchAddress == null || searchAddress.isEmpty()) ? "주소x 검색o" : "주소o 검색" + ((keyword == null || keyword.isEmpty()) ? "x" : "o");
        return ResponseDto.setSuccess(successMessage, recruitmentDtoList);
    }


    // 게시글 수정 -> 게시글 id를 받아서 해당 게시글을 수정함(리스트로 여러개 있기 때문)
    // 토큰 값이랑 현재 회원이랑 같은지 판단 해야됨
    public ResponseDto<RecruitmentDto> editPost(UpdateRecruitmentDto dto, Long id, String email) {

        memberService.validateExistMember(email);

        // 해당 게시글을 id로 조회함
        Recruitment recruitment = validateExistPost(id);

        // 회원 권한 검증
        if (validateAuthorizedMember(email, recruitment))
            return ResponseDto.setFail("해당 모집게시글의 수정 권한이 없습니다.");

        // 해당 게시글을 업데이트 시킴
        recruitment.updatePost(dto);

        recruitmentRepository.save(recruitment);

        RecruitmentDto recruitmentDto = new RecruitmentDto(recruitment);

        log.info("************************* {}의 {}번 모집게시글이 수정되었습니다. *************************", email, recruitment.getRecruitmentId());
        return ResponseDto.setSuccess("게시글이 수정되었습니다.", recruitmentDto);
    }

    // 게시글 삭제
    public ResponseDto<String> deletePost(String email, Long id) {

        memberService.validateExistMember(email);

        Recruitment recruitment = validateExistPost(id);

        // 회원 검증
        if (validateAuthorizedMember(email, recruitment))
            return ResponseDto.setFail("해당 게시글의 삭제 권한이 없습니다.");

        recruitmentRepository.delete(recruitment);

        log.info("************************* {}의 {}번 모집게시글이 삭제되었습니다. *************************", email, recruitment.getRecruitmentId());
        return ResponseDto.setSuccess("게시글이 삭제되었습니다.", null);
    }

    // 스터디 참여 여부 처리
    public ResponseDto<?> participate(String email, Long id, Boolean isParticipating) {

        memberService.validateExistMember(email);

        Recruitment recruitment = validateExistPost(id);

        if (isParticipating)
            return addMemberInPost(recruitment, email);
        else
            return subtractMemberInPost(recruitment, email);
    }

    // 참여 회원 추가
    public ResponseDto<?> addMemberInPost(Recruitment recruitment, String email) {

        int count = recruitment.getCount();
        int currentCount = recruitment.getCurrentCount();

        if (count == currentCount) {
            log.info("************************* {}번 모집게시글 더 이상 참여 불가 *************************", recruitment.getRecruitmentId());
            return ResponseDto.setSuccess("더 이상 참여할 수 없습니다.", -1);
        }

        if (isParticipantExist(recruitment, email)) {
            log.info("************************* {}회원, {}번 모집게시글은 이미 참여한 게시글 *************************", recruitment.getRecruitmentId(), email);
            return ResponseDto.setFail("이미 참여하였습니다.");
        } else if (count - 1 == currentCount) {
            ++currentCount;
            updateMemberInPost(recruitment, email, currentCount, true);
            log.info("************************* {}회원, {}번 모집게시글은 마지막 회원 참여 *************************", recruitment.getRecruitmentId(), email);
            return chatRoomService.createOrGetChatRoom(recruitment);
        } else {
            ++currentCount;
            updateMemberInPost(recruitment, email, currentCount, true);
            log.info("************************* {}회원, {}번 모집게시글 인원 추가 *************************", recruitment.getRecruitmentId(), email);
            return ResponseDto.setSuccess("인원이 추가되었습니다.", currentCount);
        }

    }

    // 참여 회원 삭제
    public ResponseDto<Object> subtractMemberInPost(Recruitment recruitment, String email) {

        memberService.validateExistMember(email);

        int count = recruitment.getCount();
        int currentCount = recruitment.getCurrentCount();

        if (count == currentCount) {
            log.info("************************* {}번 모집게시글 모집 완료 *************************", recruitment.getRecruitmentId());
            return ResponseDto.setSuccess("모집이 완료되었습니다.", -1);
        }

        if (isParticipantExist(recruitment, email)) {
            --currentCount;
            updateMemberInPost(recruitment, email, currentCount, false);
            log.info("************************* {}회원, {}번 모집게시글 참여 취소 *************************", recruitment.getRecruitmentId(), email);
            return ResponseDto.setSuccess("취소되었습니다.", currentCount);
        } else {
            log.info("************************* {}회원, {}번 모집게시글은 이미 취소하거나 참여하지 않음 *************************", recruitment.getRecruitmentId(), email);
            return ResponseDto.setFail("이미 취소하였거나 참여하지 않았습니다.");
        }
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
    private boolean validateAuthorizedMember(String email, Recruitment recruitment) {
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String memberEmail = findMember.getEmail();

        // 게시글
        String recruitmentEmail = recruitment.getMember().getEmail();

        return !memberEmail.equals(recruitmentEmail); // 같지 않으면 false를 반환
    }

}