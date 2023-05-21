package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatRoomDto;
import CodeConnect.CodeConnect.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatRoom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 가입한 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseDto<List<ChatRoomDto>> getChatRoomList(@AuthenticationPrincipal String email) {
        return chatRoomService.getChatRoomList(email);
    }

    // 단일 채팅방 조회
    @GetMapping("/{id}")
    public ResponseDto<ChatRoomDto> getChatRoom(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return chatRoomService.getChatRoom(email, id);
    }

    // 채팅방 나가기
    @DeleteMapping("/{id}")
    public ResponseDto<?> leaveChatRoom(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return chatRoomService.leaveChatRoom(email, id);
    }
}
