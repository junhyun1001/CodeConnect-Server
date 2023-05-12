package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import CodeConnect.CodeConnect.repository.CommentRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.QnaRepository;
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
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    private final CommentRepository commentRepository;


    @Transactional
    public ResponseDto<Qna> writeQna(QnaRequestDto dto, String email) {

        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();
        String title = dto.getTitle();
        String content = dto.getContent();

        Qna qna = new Qna(dto, nickname, title, content);
        qna.setTitle(title);
        qna.setNickname(nickname);
        qna.setContent(content);

        findMember.setQnas(qna);

        Qna saveQna = qnaRepository.save(qna);

        return ResponseDto.setSuccess("QnA 글 작성 성공", saveQna);
    }


    //q&a 들어갔을때 전체 조회
    public ResponseDto<List<Qna>> findQna() {
        List<Qna> qnaList = qnaRepository.findAllByOrderByCurrentDateTimeDesc();
        return ResponseDto.setSuccess("QnA 전체 글 조회 성공", qnaList);
    }

    //상세조회
    public ResponseDto<Map<Role, Object>> findOne(Long qnaId, String email) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);
        List<Comment> comments = commentRepository.findByQna(qna);

        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        // 회원 검증 후 내 게시글이면 HOST, 아니면 GUEST
        Map<Role, Object> qnaMap = new LinkedHashMap<>();
        if (validateMember(email, qna)) {
            // 자신이 작성한 글인 경우
            qnaMap.put(Role.HOST, qna);
            log.info("************************* HOST로 게시글 조회 *************************");
        } else {
            // 자신이 작성하지 않은 글인 경우
            qnaMap.put(Role.GUEST, qna);
            log.info("************************* GUEST로 게시글 조회 *************************");
        }

// comment를 하나씩 검사하여 ROLE을 지정하고 qnaMap에 put
        List<Map<String, Object>> commentHostList = new ArrayList<>();
        List<Map<String, Object>> commentGuestList = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> commentMap = new LinkedHashMap<>();
            if (validateMember2(email, Collections.singletonList(comment))) {
                commentMap.put("commentId", comment.getCommentId());
                commentMap.put("nickname", comment.getNickname());
                commentMap.put("comment", comment.getComment());
                commentMap.put("currentDateTime", comment.getCurrentDateTime());
                commentMap.put("modifiedDateTime", comment.getModifiedDateTime());
                commentMap.put("cocommentCount", comment.getCocommentCount());
                commentMap.put("role", Role.COMMENT_HOST);

                commentHostList.add(commentMap);
            } else {
                commentMap.put("commentId", comment.getCommentId());
                commentMap.put("nickname", comment.getNickname());
                commentMap.put("comment", comment.getComment());
                commentMap.put("currentDateTime", comment.getCurrentDateTime());
                commentMap.put("modifiedDateTime", comment.getModifiedDateTime());
                commentMap.put("cocommentCount", comment.getCocommentCount());
                commentMap.put("role", Role.COMMENT_GUEST);

                commentGuestList.add(commentMap);
            }
        }

        if (!commentHostList.isEmpty()) {
            qnaMap.put(Role.COMMENT_HOST, commentHostList);
        }

        if (!commentGuestList.isEmpty()) {
            qnaMap.put(Role.COMMENT_GUEST, commentGuestList);
        }

        return ResponseDto.setSuccess("게시글 조회", qnaMap);
    }
    //삭제
    @Transactional
    public ResponseDto<String> delete(Long qnaId, String email){
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(()-> new NoSuchElementException("값이 존재하지 않습니다"));

        // 회원 검증
        if (!validateMember(email, qna))
            return ResponseDto.setFail("접근 권한이 없습니다.");
        qnaRepository.delete(qna);

        return ResponseDto.setSuccess("삭제 성공", null);
    }

    //업데이트
    @Transactional
    public ResponseDto<Qna> update(Long qnaId, String title, String content, String email) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(()-> new NoSuchElementException("값이 존재하지 않습니다"));
        // 회원 검증
        if (!validateMember(email, qna))
            return ResponseDto.setFail("접근 권한이 없습니다.");
        //자바에서 직접 수정
        qna.setTitle(title); //Dirty Checking
        qna.setContent(content); //Dirty Checking
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        qna.setModifiedDateTime(String.valueOf(LocalDateTime.now().format(formatter)));
        return ResponseDto.setSuccess("업데이트 성공", qna);
    }

    //검색


    @Transactional
    public ResponseDto<List<Qna>> search(String text) {
        List<Qna> qnaList = qnaRepository.findByTitleContainingOrContentContainingOrderByCurrentDateTimeDesc(text, text);
        return ResponseDto.setSuccess("검색 성공", qnaList);
    }

    private boolean validateMember(String email, Qna qna) {
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        // Qna
        String recruitmentNickname = qna.getNickname();

        return findMemberNickname.equals(recruitmentNickname);
    }


    private boolean validateMember2(String email, List<Comment> comments) {
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        for (Comment comment : comments) {
            String commentNickname = comment.getNickname();
            if (findMemberNickname.equals(commentNickname)) {
                return true; // COMMENT_HOST
            }
        }
        return false; // COMMENT_GUEST
    }

}