package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.domain.post.Cocomment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.post.comment.CocommentRequestDto;
import CodeConnect.CodeConnect.service.CocommentService;
import CodeConnect.CodeConnect.service.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cocomment")
@RequiredArgsConstructor
public class CocommentController {
    private final CocommentService cocommentService;

    @PostMapping("/create/{commentId}")
    public ResponseDto<Cocomment> createCocomment(@PathVariable("commentId") Long commentId, @RequestBody CocommentRequestDto dto, @AuthenticationPrincipal String email){
        return cocommentService.createCocomment(commentId,dto, email);
    }

    @GetMapping("/detail/{commentId}")
    public ResponseDto<Map<Role,Object>> cocomment_detail(@PathVariable("commentId") Long commentId,@AuthenticationPrincipal String email){
        return cocommentService.detailComment(commentId,email);
    }
    @PutMapping("/update/{cocommentId}")
    public ResponseDto<Cocomment> cocomment_update(@PathVariable("cocommentId") Long cocommentId, @RequestBody CocommentRequestDto dto, @AuthenticationPrincipal String email){
        return cocommentService.update(cocommentId,dto.getCocomment(),email);
    }
    @DeleteMapping("/delete/{cocommentId}")
    public ResponseDto<String> cocomment_delete(@PathVariable("cocommentId") Long cocommentId, @AuthenticationPrincipal String email){
        return cocommentService.delete(cocommentId,email);
    }
}
