package CodeConnect.CodeConnect.controller;


import CodeConnect.CodeConnect.domain.post.Post;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 새 글 작성
    @PostMapping("/new")
    public ResponseDto<Post> writePost(@RequestBody Post post) {
        return postService.writePost(post);
    }

    // 글 목록 조회
    @GetMapping("/list")
    public ResponseDto<List<Post>> getList(@RequestBody Post post) {
//        return null;
        return postService.getListByAddressAndField(post);
    }

}
