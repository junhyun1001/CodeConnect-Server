package CodeConnect.CodeConnect.controller;


import CodeConnect.CodeConnect.domain.post.Post;
import CodeConnect.CodeConnect.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    // 새 글 작성
    @PostMapping("/new")
    public ResponseDto<Post> writePost() {
        return null;
    }

    // 글 목록 조회
    @GetMapping("/list")
    public ResponseDto<List<Post>> getList() {
        return null;
    }

}
