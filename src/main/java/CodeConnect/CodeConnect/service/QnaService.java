package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.QnaDto;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentRequestDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import CodeConnect.CodeConnect.repository.CommentRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final이 붙은 애들을 자동으로 주입해줌
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    //업데이트

    @Transactional
    public ResponseDto<Qna> writeQna(QnaRequestDto dto, String email){

        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();

        Qna qna = new Qna(dto, nickname);

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
    public ResponseDto<?> delete(Long qnaId){
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(()-> new NoSuchElementException("값이 존재하지 않습니다"));
        qnaRepository.delete(qna);
        return ResponseDto.setSuccess("삭제 성공", null);
    }

    //업데이트
    @Transactional
    public ResponseDto<?> update(Long qnaId, String title, String content){
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);
        //자바에서 직접 수정
        qna.setTitle(title); //Dirty Checking
        qna.setContent(content); //Dirty Checking
        return ResponseDto.setSuccess("업데이트 성공", qna);
    }
    // 댓글 추가
//    public ResponseDto<?> addComment(Long qnaId, CommentRequestDto commentRequestDto, String email) {
//        Optional<Qna> optionalQna = qnaRepository.findById(qnaId);
//        if (optionalQna.isEmpty()) {
//            return ResponseDto.setFail("게시글을 찾을 수 없습니다.");
//        }
//
//        Qna qna = optionalQna.get();
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("잘못된 회원 정보입니다."));
//
//        Comment comment = Comment.builder()
//                .comment(commentRequestDto.getComment())
//                .member(member)
//                .currentDateTime(LocalDateTime.now())
//                .build();
//
//        qna.getComments().add(comment);
//        qnaRepository.save(qna);
//
//        return ResponseDto.setSuccess("댓글 작성 성공", comment);
//    }

}