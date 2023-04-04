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

import java.util.List;

/**
 * 게시글 쓰기
 * 게시글 수정
 * 게시글 삭제
 * 주소와 관심분야 기준으로 리스트 조회
 */

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;

    //
    public ResponseDto<Recruitment> createPost(CreateRecruitmentDto dto, String email) {

        Member findMember = memberRepository.findByEmail(email); // 토큰에 포함된 email 값으로 회원 조회
        String nickname = findMember.getNickname();
        String address = findMember.getAddress();

        Recruitment recruitment = new Recruitment(dto, nickname, address); // 받아온 게시글 데이터로 게시글 생성

        Recruitment savedRecruitment = recruitmentRepository.save(recruitment); // 게시글 영속화

        return ResponseDto.setSuccess("글 저장 성공", savedRecruitment);
    }

    public ResponseDto<List<Recruitment>> getPostsByAddressAndField(String email) {
        // 글을 작성한 회원의 정보
        Member findMember = memberRepository.findByEmail(email);
        String nickname = findMember.getNickname();
        String address = findMember.getAddress();
        List<String> fieldList = findMember.getFieldList();

        // 게시글 정보의 관심분야
//        Recruitment findRecruitment = recruitmentRepository.findByNickname(nickname);
//        String recruitmentField = findRecruitment.getField();


//        List<Recruitment> findByAddressAndFieldRecruitmentList = null;
//        for(String findField: fieldList) {
//            if (findField.equals(recruitmentField)) {
//                findByAddressAndFieldRecruitmentList = recruitmentRepository.findByAddressAndField(address, findField);
//            }
//        }

//        List<Recruitment> recruitmentBySameAddressAndFieldAsMember = findRecruitmentBySameAddressAndFieldAsMember(nickname, fieldList);


        return ResponseDto.setSuccess("글 불러오기 성공", findRecruitmentBySameAddressAsMember(nickname)); // 주소를 기준으로 찾는것만 됨


        // 회원 정보의 관심분야 리스트와 게시글 정보의 관심분야 중에서 맞는것을 찾음
//        return ResponseDto.setSuccess("글 불러오기 성공", recruitmentBySameAddressAndFieldAsMember);
    }

    // 글을 쓴 회원 정보의 주소값과 게시글 정보의 주소값을 비교해서 같은 리스트를 반환해줌
    public List<Recruitment> findRecruitmentBySameAddressAsMember(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname);
        if (findMember == null) {
            return null;
        }
        String address = findMember.getAddress();

        return recruitmentRepository.findByAddress(address);
    }

//    public List<Recruitment> findRecruitmentBySameAddressAndFieldAsMember(String nickname, List<String> fieldList) {
//        Member findMember = memberRepository.findByNickname(nickname);
//        if (findMember == null) {
//            return null;
//        }
//        String address = findMember.getAddress();
//
//        // 회원 정보의 관심분야 리스트와 게시글 정보의 관심분야 중에서 맞는것을 찾음
//
//        return recruitmentRepository.findByAddressAndField(address, fieldList.toString());
//    }

    // 게시글 수정
//    public ResponseDto<Recruitment> editPost(EditRecruitmentDto dto, String email) {
//
//        String title = dto.getTitle();
//        String content = dto.getContent();
//        int count = dto.getCount();
//        String field = dto.getField();
//
//        return null;
//    }


}