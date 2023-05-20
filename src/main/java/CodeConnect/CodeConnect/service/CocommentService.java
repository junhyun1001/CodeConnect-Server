package CodeConnect.CodeConnect.service;

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
import java.time.format.DateTimeFormatter;
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
        cocomment.setCocomment(dto.getCocomment());
        cocomment.setNickname(nickname);
        cocomment.setCocommentId(dto.getCocommentId());
        cocomment.setComment(comment);

        if(cocomment.getCocommentId() != null){
            cocomment.setCocommentId(null);
        }
        comment.setCocommentCount(comment.getCocommentCount()+1);
        Cocomment savedCocomment = cocommentRepository.save(cocomment);
        return ResponseDto.setSuccess("대댓글 작성 완료", savedCocomment);
    }

    public ResponseDto<Map<Role,Object>> detailComment(Long commentId, String email){
        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(NullPointerException::new);
        List<Cocomment> cocommentList = cocommentRepository.findAllByCommentOrderByCurrentDateTimeDesc(comment);

        Map<Role, Object> cocommentMap = new LinkedHashMap<>();

        // comment를 하나씩 검사하여 ROLE을 지정하고 qnaMap에 put
        List<Cocomment> cocommentHostList = new ArrayList<>();
        List<Cocomment> cocommentGuestList = new ArrayList<>();
        for (Cocomment cocomment : cocommentList) {
            if (validateMember2(email, Collections.singletonList(cocomment))) {
                cocommentHostList.add(cocomment);
            } else {
                cocommentGuestList.add(cocomment);
            }
        }
        if (!cocommentHostList.isEmpty()) {
            cocommentMap.put(Role.COCOMMENT_HOST, cocommentHostList);
        }
        if (!cocommentGuestList.isEmpty()) {
            cocommentMap.put(Role.COCOMMENT_GUEST, cocommentGuestList);
        }

        return ResponseDto.setSuccess("게시글 조회", cocommentMap);
    }
    @Transactional
    public ResponseDto<Cocomment> update(Long cocomentId,String cocomment, String email) {
        Cocomment cocomments = cocommentRepository.findById(cocomentId).orElseThrow(NullPointerException::new);
        // 회원 검증
        if (!validateMember(email, cocomments))
            return ResponseDto.setFail("접근 권한이 없습니다");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        cocomments.setModifiedDateTime(LocalDateTime.now().format(formatter));
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
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        // Qna
        String recruitmentNickname = cocomment.getNickname();

        return findMemberNickname.equals(recruitmentNickname);
    }
    private boolean validateMember2(String email, List<Cocomment> cocomments) {
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        for (Cocomment cocomment : cocomments) {
            String commentNickname = cocomment.getNickname();
            if (findMemberNickname.equals(commentNickname)) {
                return true; // COCOMMENT_HOST
            }
        }
        return false; // COCOMMENT_GUEST
    }

}
