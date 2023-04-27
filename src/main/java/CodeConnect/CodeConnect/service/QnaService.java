package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.QnaDto;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import CodeConnect.CodeConnect.repository.CommentRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final이 붙은 애들을 자동으로 주입해줌
public class QnaService {

    private final QnaRepository qnaRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    //업데이트

    @Transactional
    public ResponseDto<Qna> writeQna(QnaRequestDto dto, String email){

        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();
        String title = dto.getTitle();
        String content = dto.getContent();

        Qna qna = new Qna();
        qna.setTitle(title);
        qna.setNickname(nickname);
        qna.setContent(content);

        Qna saveQna = qnaRepository.save(qna);

        return ResponseDto.setSuccess("QnA 글 작성 성공", saveQna);
    }



    //q&a 들어갔을때 전체 조회
    public ResponseDto<List<QnaDto>> findQna() {
        List<Qna> qnaList = qnaRepository.findAll();
        List<QnaDto> qnaDtoList = qnaList.stream()
                .map(QnaDto::new)
                .collect(Collectors.toList());
        return ResponseDto.setSuccess("QnA 전체 글 조회 성공", qnaDtoList);
    }

    //상세조회
    public ResponseDto<Qna> findOne(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);
        return ResponseDto.setSuccess("QnA 글 상세 조회 성공", qna);
    }
    //삭제
    @Transactional
    public ResponseDto<?> delete(Long qnaId, String email){
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(()-> new NoSuchElementException("값이 존재하지 않습니다"));

        // 회원 검증
        if (validateMember(email, qna))
            return ResponseDto.setFail("접근 권한이 없습니다.");
        qnaRepository.delete(qna);

        return ResponseDto.setSuccess("삭제 성공", null);
    }

    //업데이트
    @Transactional
    public ResponseDto<?> update(Long qnaId, String title, String content, String email){
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);
        // 회원 검증
        if (validateMember(email, qna))
            return ResponseDto.setFail("접근 권한이 없습니다.");
        //자바에서 직접 수정
        qna.setTitle(title); //Dirty Checking
        qna.setContent(content); //Dirty Checking
        return ResponseDto.setSuccess("업데이트 성공", qna);
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