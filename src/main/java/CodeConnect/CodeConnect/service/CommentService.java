package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.Member;
import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentRequestDto;
import CodeConnect.CodeConnect.dto.post.comment.CommentResponseDto;
import CodeConnect.CodeConnect.repository.CommentRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor // final이 붙은 애들을 자동으로 주입해줌
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    private final QnaRepository qnaRepository;

    @Transactional
    public ResponseDto<Comment> createComment(Long qnaId, CommentRequestDto requestDto, String email) {

        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();

        Optional<Qna> optionalQna = qnaRepository.findById(qnaId);
        if (optionalQna.isEmpty()) {
            return null;
        }
        Qna qna = optionalQna.get();

        Comment comment = new Comment();

        comment.setComment(requestDto.getComment());
        comment.setNickname(nickname);
        comment.setCommentId(requestDto.getCommentId());
        comment.setQna(qna);



        if (comment.getCommentId() != null) {
            comment.setCommentId(null);
        }

        qna.setCommentCount(qna.getCommentCount() + 1);

        Comment savedComment = commentRepository.save(comment);

        return ResponseDto.setSuccess("댓글 쓰기 성공", savedComment);
    }
    //특정 Qna 들어왔을때 댓글 조회
    public ResponseDto<List<CommentResponseDto>> findComment(Long qnaId, String email) {
        // 해당 회원 검증
        Optional<Member> findMember = memberRepository.findById(email);
        if (findMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }
        // 해당 Qna 검증
        Optional<Qna> findQna = qnaRepository.findById(qnaId);
        if (findQna.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 Qna입니다.");
        }

        //특정한 findQna에 달린 댓글을 commentList에 할당
        // Optional내부에 있는 Comment객체를 가져옴
        List<Comment> commentList = commentRepository.findByQna(findQna.get());
        List<CommentResponseDto> commentResponseDtoList = commentList.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("해당 qna 댓글 조회 성공", commentResponseDtoList);
    }
    //댓글 삭제
    public ResponseDto<?> deleteComment(Long commentId, String email){

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다"));
        if (validateMember(email, comment))
            return ResponseDto.setFail("접근 권한이 없습니다");
        commentRepository.delete(comment);

        return ResponseDto.setSuccess("댓글 삭제 성공", null);
    }

    //댓글 수정
    public ResponseDto<?> updateComment(Long commentId, String email, String comments){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다"));
        if (validateMember(email, comment))
            return ResponseDto.setFail("접근 권한이 없습니다");
        comment.setComment(comments);
        return ResponseDto.setSuccess("댓글 수정 성공", comment);
    }

    private boolean validateMember(String email, Comment comment) {
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        // 댓글
        String commentNickname = comment.getNickname();
        return !findMemberNickname.equals(commentNickname);
    }
}

