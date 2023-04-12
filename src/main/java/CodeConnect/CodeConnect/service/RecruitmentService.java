package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.recruitment.CreateRecruitmentDto;
import CodeConnect.CodeConnect.dto.post.recruitment.EditRecruitmentDto;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 게시글 쓰기
 * <p>
 * 게시글 수정
 * <p>
 * 게시글 삭제
 * <p>
 * 주소와 관심분야 기준으로 리스트 조회
 * <p>
 * 게시글 전체 조회
 */


@Service
@RequiredArgsConstructor
@Transactional
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

        return ResponseDto.setSuccess("게시글이 저장되었습니다.", savedRecruitment);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public ResponseDto<List<Recruitment>> getAllPosts() {

        List<Recruitment> findAllRecruitments = recruitmentRepository.findAll();

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

        return recruitmentRepository.findByAddress(address);
    }

    // 게시글 단일 조회
    public ResponseDto<Map<Role, Recruitment>> getPost(String email, Long id) {

        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if (optionalRecruitment.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 게시글 입니다.");
        }

        Recruitment recruitment = optionalRecruitment.get();

        // 회원 검증 후 내 게시글이면 true, 아니면 false
        Map<Role, Recruitment> recruitmentMap = new HashMap<>();
        if (validateMember(email, recruitment)) { // false 값이 반환 될 때
            recruitmentMap.put(Role.GUEST, recruitment);
            return ResponseDto.setSuccess("GUEST 게시글 조회", recruitmentMap);
        }
        else {
            recruitmentMap.put(Role.HOST, recruitment);
            return ResponseDto.setSuccess("HOST 게시글 조회", recruitmentMap);
        }

    }

    // 게시글 수정 -> 게시글 id를 받아서 해당 게시글을 수정함(리스트로 여러개 있기 때문)
    // 토큰 값이랑 현재 회원이랑 같은지 판단 해야됨
    public ResponseDto<Recruitment> editPost(EditRecruitmentDto dto, Long id, String email) {

        // 수정 할 목록들을 dto로 받아옴
        String title = dto.getTitle();
        String content = dto.getContent();
        int count = dto.getCount();
        String field = dto.getField();

        // 해당 게시글을 id로 조회함
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if (optionalRecruitment.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 게시글 입니다.");
        }

        Recruitment recruitment = optionalRecruitment.get();

        // 회원 검증
        if (validateMember(email, recruitment))
            return ResponseDto.setFail("접근 권한이 없습니다.");

        // 해당 게시글을 업데이트 시킴
        recruitment.setTitle(title);
        recruitment.setContent(content);
        recruitment.setCount(count);
        recruitment.setField(field);
        recruitmentRepository.save(recruitment);
        return ResponseDto.setSuccess("게시글이 수정되었습니다.", recruitment);
    }

    // 게시글 삭제
    public ResponseDto<String> deletePost(String email, Long id) {

        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(id);
        if (optionalRecruitment.isEmpty())
            return ResponseDto.setFail("존재하지 않는 게시글 입니다.");

        Recruitment recruitment = optionalRecruitment.get();

        // 회원 검증
        if (validateMember(email, recruitment))
            return ResponseDto.setFail("접근 권한이 없습니다.");
        recruitmentRepository.delete(recruitment);

        return ResponseDto.setFail("게시글이 삭제되었습니다.");
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