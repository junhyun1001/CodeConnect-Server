package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Cocomment;
import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CocommentRequestDto;
import CodeConnect.CodeConnect.repository.CocommentRepository;
import CodeConnect.CodeConnect.repository.CommentRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor // final이 붙은 애들을 자동으로 주입해줌
public class CocommentService {
    private final CocommentRepository cocommentRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    @Transactional
    public ResponseDto<Cocomment> createCocomment(Long commentId, CocommentRequestDto dto, String email){
        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            return null;
        }
        Comment comment = optionalComment.get();

        Cocomment cocomment = new Cocomment(nickname, email);
        cocomment.setMember(findMember);
        cocomment.setCocomment(dto.getCocomment());
        cocomment.setNickname(nickname);
        cocomment.setCocommentId(dto.getCocommentId());
        cocomment.setProfileImagePath(comment.getMember().getProfileImagePath());
        cocomment.setComment(comment);

        if(cocomment.getCocommentId() != null){
            cocomment.setCocommentId(null);
        }
        comment.setCocommentCount(comment.getCocommentCount()+1);
        Cocomment savedCocomment = cocommentRepository.save(cocomment);
        return ResponseDto.setSuccess("대댓글 작성 완료", savedCocomment);
    }
    @Transactional
    public ResponseDto<Map<Role,Object>> detailComment(Long commentId, String email){
        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }
        Member member = optionalMember.get();
        Comment comment = commentRepository.findById(commentId).orElseThrow(NullPointerException::new);
        List<Cocomment> cocommentList = cocommentRepository.findAllByCommentOrderByCurrentDateTimeDesc(comment);

        Map<Role, Object> cocommentMap = new LinkedHashMap<>();

        // comment를 하나씩 검사하여 ROLE을 지정하고 cocommentMap에 put
        List<CocommentRequestDto> cocommentHostDtoList = new ArrayList<>();
        List<CocommentRequestDto> cocommentGuestDtoList = new ArrayList<>();
        for (Cocomment cocomment : cocommentList) {
            CocommentRequestDto cocommentDto = new CocommentRequestDto(cocomment);
            if (validateMember2(Collections.singletonList(cocomment), member)) {
                cocommentDto.setProfileImagePath(cocomment.getMember().getProfileImagePath());
                cocommentHostDtoList.add(cocommentDto);
            } else {
                cocommentDto.setProfileImagePath(cocomment.getMember().getProfileImagePath());
                cocommentGuestDtoList.add(cocommentDto);
            }
        }
        if (!cocommentHostDtoList.isEmpty()) {
            cocommentMap.put(Role.COCOMMENT_HOST, cocommentHostDtoList);
        }
        if (!cocommentGuestDtoList.isEmpty()) {
            cocommentMap.put(Role.COCOMMENT_GUEST, cocommentGuestDtoList);
        }

        return ResponseDto.setSuccess("게시글 조회", cocommentMap);
    }
    @Transactional
    public ResponseDto<Cocomment> update(Long cocomentId,String cocomment, String email) {
        Member findMember = memberRepository.findByEmail(email);
        Cocomment cocomments = cocommentRepository.findById(cocomentId).orElseThrow(NullPointerException::new);
        // 회원 검증
        if (!validateMember(email, cocomments))
            return ResponseDto.setFail("접근 권한이 없습니다");
        cocomments.setMember(findMember);
        cocomments.setProfileImagePath(cocomments.getMember().getProfileImagePath());
        cocomments.setCocomment(cocomment);
        return ResponseDto.setSuccess("대댓글 수정 성공", cocomments);
    }
    //댓글 삭제
    @Transactional
    public ResponseDto<String> delete(Long cocommentId, String email){

        Cocomment cocomment = cocommentRepository.findById(cocommentId).orElseThrow(() -> new NoSuchElementException("대댓글이 존재하지 않습니다"));
        if (!validateMember(email, cocomment))
            return ResponseDto.setFail("접근 권한이 없습니다");

        Comment comment = commentRepository.findById(cocomment.getComment().getCommentId()).orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다"));
        comment.setCocommentCount(comment.getCocommentCount()-1);
        cocommentRepository.delete(cocomment);

        return ResponseDto.setSuccess("대댓글 삭제 성공", null);
    }
    private boolean validateMember(String email, Cocomment cocomment) {
        Member findMember = memberRepository.findByEmail(email);
        String memberEmail = cocomment.getMember().getEmail();

        if(findMember.getEmail().equals(memberEmail)){
            return true;
        }
        return false;

    }
    private boolean validateMember2(List<Cocomment> cocomments, Member member) {
        String findMemberNickname = member.getNickname();

        for (Cocomment cocomment : cocomments) {
            String commentNickname = cocomment.getMember().getNickname();
            if (findMemberNickname.equals(commentNickname)) {
                return true; // COCOMMENT_HOST
            }
        }
        return false; // COCOMMENT_GUEST
    }

}
