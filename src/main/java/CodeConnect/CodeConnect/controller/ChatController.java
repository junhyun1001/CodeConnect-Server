package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.chat.ChatRequestDto;
import CodeConnect.CodeConnect.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template; // 특정 Broker로 메세지 전달
    private final ChatService chatService;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping("/chat/enter")
    public void enter(ChatRequestDto chatRequestDto) {
        chatRequestDto.setMessage(chatRequestDto.getNickname() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + chatRequestDto.getRoomId(), chatRequestDto);
    }

    @MessageMapping("/chat/message")
    public void message(ChatRequestDto chatRequestDto) {
        template.convertAndSend("/sub/chat/room/" + chatRequestDto.getRoomId(), chatRequestDto);
        chatService.saveChat(chatRequestDto);
    }

}

