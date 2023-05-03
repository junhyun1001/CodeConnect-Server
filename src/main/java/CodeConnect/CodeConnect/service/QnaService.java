package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
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
//        List<QnaDto> qnaDtoList = qnaList.stream()
//                .map(QnaDto::new)
//                .collect(Collectors.toList());
        return ResponseDto.setSuccess("QnA 전체 글 조회 성공", qnaList);
    }

    //상세조회
    public ResponseDto<Map<Role, Qna>> findOne(Long qnaId, String email) {
//        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);
//        return ResponseDto.setSuccess("QnA 글 상세 조회 성공", qna);
        Optional<Member> optionalMember = memberRepository.findById(email);
        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }

        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);

        // 회원 검증 후 내 게시글이면 HOST, 아니면 GUEST
        Map<Role, Qna> qnaMap = new HashMap<>();
        if (validateMember(email, qna)) { // false 값이 반환 될 때
            qnaMap.put(Role.GUEST, qna);
            log.info("************************* GUEST로 게시글 조회 *************************");
            return ResponseDto.setSuccess("GUEST 게시글 조회", qnaMap);
        } else {
            qnaMap.put(Role.HOST, qna);
            log.info("************************* HOST로 게시글 조회 *************************");
            return ResponseDto.setSuccess("HOST 게시글 조회", qnaMap);
        }
    }

    //삭제
    @Transactional
    public ResponseDto<String> delete(Long qnaId, String email) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(() -> new NoSuchElementException("값이 존재하지 않습니다"));

        // 회원 검증
        if (validateMember(email, qna))
            return ResponseDto.setFail("접근 권한이 없습니다.");
        qnaRepository.delete(qna);

        return ResponseDto.setSuccess("삭제 성공", null);
    }

    //업데이트
    @Transactional
    public ResponseDto<Qna> update(Long qnaId, String title, String content, String email) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);
        // 회원 검증
        if (validateMember(email, qna))
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
//        List<QnaRequestDto> qnaRequestDtoList = qnaList.stream()
//                .map(QnaRequestDto::new)
//                .collect(Collectors.toList());
        return ResponseDto.setSuccess("검색 성공", qnaList);
    }

    private boolean validateMember(String email, Qna qna) {
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        // Qna
        String recruitmentNickname = qna.getNickname();

        return !findMemberNickname.equals(recruitmentNickname);
    }
}