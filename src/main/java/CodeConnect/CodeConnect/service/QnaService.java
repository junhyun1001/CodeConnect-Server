package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.utils.Base64Converter;
import CodeConnect.CodeConnect.utils.EntityToDto;
import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Comment;
import CodeConnect.CodeConnect.domain.post.Qna;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaDto;
import CodeConnect.CodeConnect.dto.post.qna.QnaRequestDto;
import CodeConnect.CodeConnect.repository.CommentRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor // final이 붙은 애들을 자동으로 주입해줌
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseDto<QnaDto> writeQna(QnaRequestDto dto, String email) {
        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();
        String title = dto.getTitle();
        String content = dto.getContent();

        Qna qna = new Qna(dto, nickname, title, content);

        // 이미지 데이터 처리
        String base64Image = dto.getBase64Image();
        if (base64Image != null && !base64Image.isEmpty()) {
            // 이미지 파일로 저장하고 파일 경로 설정
            String filePath = Base64Converter.saveImageFromBase64("qna", base64Image);
            if (filePath != null) {
                qna.setImagePath(filePath);
            } else {
                // 이미지 파일 저장 실패 시 예외 처리 방법을 선택하거나 오류 메시지 반환
                return ResponseDto.setFail("이미지 파일 저장에 실패했습니다.");
            }
        }
        qna.setMember(findMember);
        qna.setTitle(title);
        qna.setNickname(nickname);
        qna.setContent(content);
        qna.setProfileImagePath(qna.getMember().getProfileImagePath()); // Member 엔티티에서 profileImagePath 설정

        findMember.setQna(qna);
        Qna saveQna = qnaRepository.save(qna);

        QnaDto qnaDto = new QnaDto(qna);

        log.info("******************** Q&A 글 작성");

        return ResponseDto.setSuccess("QnA 글 작성 성공", qnaDto);
    }

    //q&a 들어갔을때 전체 조회
    @Transactional
    public ResponseDto<List<QnaDto>> findQna() {
        List<Qna> qnaList = qnaRepository.findAllByOrderByCurrentDateTimeDesc();
        for (Qna qna : qnaList) {
            qna.setProfileImagePath(qna.getMember().getProfileImagePath()); // Member 엔티티에서 profileImagePath 설정
        }

        List<QnaDto> qnaDtos = EntityToDto.mapListToDto(qnaList, QnaDto::new);
        return ResponseDto.setSuccess("QnA 전체 글 조회 성공", qnaDtos);
    }

    //상세조회
    @Transactional
    public ResponseDto<Map<Role, Object>> findOne(Long qnaId, String email) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NullPointerException::new);
        List<Comment> comments = commentRepository.findAllByQnaOrderByCurrentDateTimeDesc(qna);
        Optional<Member> optionalMember = memberRepository.findById(email);

        if (optionalMember.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 회원입니다.");
        }


        Member member = optionalMember.get();
        // 회원 검증 후 내 게시글이면 HOST, 아니면 GUEST
        Map<Role, Object> qnaMap = new LinkedHashMap<>();
        if (validateMember(email, qna)) {
            qna.setProfileImagePath(member.getProfileImagePath());
            // 자신이 작성한 글인 경우
            QnaDto qnaDto = new QnaDto(qna);
            qnaMap.put(Role.HOST, qnaDto);
            log.info("************************* HOST로 게시글 조회 *************************");
        } else {
            qna.setProfileImagePath(qna.getMember().getProfileImagePath());
            // 자신이 작성하지 않은 글인 경우
            QnaDto qnaDto = new QnaDto(qna);
            qnaMap.put(Role.GUEST, qnaDto);
            log.info("************************* GUEST로 게시글 조회 *************************");
        }

        List<Map<String, Object>> commentHostList = new ArrayList<>();
        List<Map<String, Object>> commentGuestList = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> commentMap = new LinkedHashMap<>();
            if (validateMember2(Collections.singletonList(comment), member)) {
                putMap(comment, commentMap);
                commentMap.put("role", Role.COMMENT_HOST);

                commentHostList.add(commentMap);
            } else {
                putMap(comment, commentMap);
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

    private void putMap(Comment comment, Map<String, Object> commentMap) {
        commentMap.put("commentId", comment.getCommentId());
        commentMap.put("nickname", comment.getMember().getNickname()); // 수정된 닉네임 사용
        commentMap.put("comment", comment.getComment());
        commentMap.put("currentDateTime", comment.getCurrentDateTime());
        commentMap.put("cocommentCount", comment.getCocommentCount());
        commentMap.put("profileImagePath", comment.getMember().getProfileImagePath());
    }

    //삭제
    @Transactional
    public ResponseDto<String> delete(Long qnaId, String email) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(() -> new NoSuchElementException("값이 존재하지 않습니다"));

        // 회원 검증
        if (!validateMember(email, qna))
            return ResponseDto.setFail("접근 권한이 없습니다.");
        qnaRepository.delete(qna);

        return ResponseDto.setSuccess("삭제 성공", null);
    }

    //업데이트
    @Transactional
    public ResponseDto<QnaDto> update(Long qnaId, String title, String content, String base64Image, String email) {
        Member findMember = memberRepository.findByEmail(email);
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new NoSuchElementException("값이 존재하지 않습니다"));

        // 회원 검증
        if (!validateMember(email, qna))
            return ResponseDto.setFail("접근 권한이 없습니다.");

        // 이미지 데이터 처리
        if (base64Image != null && !base64Image.isEmpty()) {
            // 이미지 파일로 저장하고 파일 경로 설정
            String filePath = Base64Converter.saveImageFromBase64("qna", base64Image);
            if (filePath != null) {
                // 기존 이미지 삭제
                Base64Converter.deleteImage(qna.getImagePath());
                qna.setImagePath(filePath);
            } else {
                return ResponseDto.setFail("이미지 파일 저장에 실패했습니다.");
            }
        } else {
            qna.setImagePath(null);
        }

        // 자바에서 직접 수정
        qna.setMember(findMember);
        qna.setTitle(title); // Dirty Checking
        qna.setContent(content); // Dirty Checking
        qna.setProfileImagePath(qna.getMember().getProfileImagePath()); // Member 엔티티에서 profileImagePath 설정

        QnaDto qnaDto = new QnaDto(qna);

        return ResponseDto.setSuccess("업데이트 성공", qnaDto);
    }

    //검색
    @Transactional
    public ResponseDto<List<QnaDto>> search(String text) {
        List<Qna> qnaList = qnaRepository.findByTitleContainingOrContentContainingOrderByCurrentDateTimeDesc(text, text);
        List<QnaDto> qnaDtos = EntityToDto.mapListToDto(qnaList, QnaDto::new);
        return ResponseDto.setSuccess("검색 성공", qnaDtos);
    }

    // top10 조회
    @Transactional
    public ResponseDto<List<QnaDto>> getPopularPost() {
        List<Qna> top10ByOrderByLikeCountDesc = qnaRepository.findTop10ByOrderByLikeCountDesc();
        for (Qna qna : top10ByOrderByLikeCountDesc) {
            qna.setProfileImagePath(qna.getMember().getProfileImagePath()); // Member 엔티티에서 profileImagePath 설정
        }
        List<QnaDto> qnaDtos = EntityToDto.mapListToDto(top10ByOrderByLikeCountDesc, QnaDto::new);
        return ResponseDto.setSuccess("Q&A Top10", qnaDtos);
    }

    // 좋아요 카운트
    @Transactional
    public ResponseDto<Integer> likeCounting(String email, Long qnaId) {
        Optional<Qna> optionalQna = qnaRepository.findById(qnaId);
        if (optionalQna.isEmpty()) {
            return ResponseDto.setFail("존재하지 않는 Q&A 게시글 입니다.");
        }
        Qna qna = optionalQna.get();
        int likeCount = qna.getLikeCount();

        if (isLikedQna(qna, email)) {
            --likeCount;
            updateLikeCount(qna, email, likeCount, false);
            return ResponseDto.setSuccess("Q&A 좋아요 취소", likeCount);
        } else {
            ++likeCount;
            updateLikeCount(qna, email, likeCount, true);
            return ResponseDto.setSuccess("Q&A 좋아요", likeCount);

        }
    }

    private boolean validateMember(String email, Qna qna) {
        // 회원
        Member findMember = memberRepository.findByEmail(email);
        String findMemberNickname = findMember.getNickname();

        // Qna
        String recruitmentNickname = qna.getNickname();

        return findMemberNickname.equals(recruitmentNickname);
    }


    private boolean validateMember2(List<Comment> comments, Member member) {
        String findMemberNickname = member.getNickname();

        for (Comment comment : comments) {
            String commentNickname = comment.getMember().getNickname();
            if (findMemberNickname.equals(commentNickname)) {
                return true; // COMMENT_HOST
            }
        }
        return false; // COMMENT_GUEST
    }

    // 좋아요 눌렀는지 확인
    public boolean isLikedQna(Qna qna, String email) {
        return qna.getLikesEmail().stream().anyMatch(participant -> participant.equals(email));
    }

    // 좋아요 회원 정보 업데이트
    public void updateLikeCount(Qna qna, String email, int likeCount, Boolean bool) {
        qna.setLikeCount(likeCount);
        if (bool)
            qna.getLikesEmail().add(email);
        else
            qna.getLikesEmail().remove(email);
        qnaRepository.save(qna);
    }
}