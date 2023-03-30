package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.post.Post;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public ResponseDto<Post> writePost(@RequestBody Post post) {
        Post savePost = postRepository.save(post);
        return ResponseDto.setSuccess("글 저장 성공", savePost);
    }

    public ResponseDto<List<Post>> getListByAddressAndField(Post post) {

//        List<Post> findPostByAddressAndField = postRepository.findByAddressAndFieldList(post.getAddress(), post.getFieldList());
        List<Post> findByAddress = postRepository.findByAddress(post.getAddress());
        return ResponseDto.setSuccess("글 조회 성공", findByAddress);
    }
}
