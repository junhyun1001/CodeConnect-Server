package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.EditRecruitmentDto;
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

    // 주소 기준 게시글 조회
    public ResponseDto<List<Recruitment>> getPostsByAddressAndField(String email) {
        // 글을 작성한 회원의 정보
        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();

        return ResponseDto.setSuccess("글 불러오기 성공", findRecruitmentBySameAddressMember(nickname)); // 주소를 기준으로 찾는것만 됨
    }

    // 글을 쓴 회원 정보의 주소값과 게시글 정보의 주소값을 비교해서 같은 리스트를 반환해줌
    public List<Recruitment> findRecruitmentBySameAddressMember(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname);
        if (findMember == null) {
            return null;
        }
        String address = findMember.getAddress();

        return recruitmentRepository.findByAddressOrderByCurrentDateTimeDesc(address);
    }

    // 게시글 단일 조회
    public ResponseDto<Map<Role, Recruitment>> getPost(String email, Long id) {

        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        Recruitment recruitment = validateExistPost(id);

        // 회원 검증 후 내 게시글이면 HOST, 아니면 GUEST
        Map<Role, Recruitment> recruitmentMap = new HashMap<>();
        if (validateMember(email, recruitment)) { // false 값이 반환 될 때
            recruitmentMap.put(Role.GUEST, recruitment);
            log.info("************************* GUEST로 게시글 조회 *************************");
            return ResponseDto.setSuccess("GUEST 게시글 조회", recruitmentMap);
        } else {
            recruitmentMap.put(Role.HOST, recruitment);
            log.info("************************* HOST로 게시글 조회 *************************");
            return ResponseDto.setSuccess("HOST 게시글 조회", recruitmentMap);
        }

    }

    // 게시글 수정 -> 게시글 id를 받아서 해당 게시글을 수정함(리스트로 여러개 있기 때문)
    // 토큰 값이랑 현재 회원이랑 같은지 판단 해야됨
    public ResponseDto<Recruitment> editPost(EditRecruitmentDto dto, Long id, String email) {

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

    // 참여하기에 대한 인원 수 증가
//    public ResponseDto<Recruitment> addMember(String email, Long id) {
//
//        Recruitment recruitment = validateExistPost(id);
//        int currentCount = recruitment.getCurrentCount();
//
//        if (!validateMember(email, recruitment))
//            return ResponseDto.setFail("본인은 참여할 수 없습니다.");
//
//        recruitment.addCurrentCount(currentCount, email);
//
//        recruitmentRepository.save(recruitment);
//
//        return ResponseDto.setSuccess("인원이 추가되었습니다.", recruitment);
//
//    }

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