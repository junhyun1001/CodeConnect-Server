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

    @GetMapping("/list")
    public ResponseDto<List<ChatRoomDto>> getChatRoomList(@AuthenticationPrincipal String email) {
        return chatRoomService.getChatRoomList(email);
    }

    @GetMapping("/{id}")
    public ResponseDto<ChatRoomDto> getChatRoom(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return chatRoomService.getChatRoom(email, id);
    }

    @DeleteMapping("/{id}")
    public void leaveChatRoom(@AuthenticationPrincipal String email, @PathVariable Long id) {
        chatRoomService.leaveChatRoom(email, id);
    }
}
