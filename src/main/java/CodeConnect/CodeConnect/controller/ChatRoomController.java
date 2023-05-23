package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatRoomDto;
import CodeConnect.CodeConnect.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public ResponseDto<Map<String, Object>> getChatRoom(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return chatRoomService.getChatRoom(email, id);
    }

}
