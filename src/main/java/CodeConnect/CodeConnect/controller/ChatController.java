package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.chat.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate template; // 특정 Broker로 메세지 전달

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping("/chat/enter")

    public void enter(ChatDto chatDto) {
        chatDto.setMessage(chatDto.getNickname() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
    }

    @MessageMapping("/chat/message")
    public void message(ChatDto chatDto) {
        log.info("채팅");
        log.info("roomId:{}", chatDto.getRoomId());
        log.info("nickname:{}", chatDto.getNickname());
        log.info("message:{}", chatDto.getMessage());
        template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
    }

}

