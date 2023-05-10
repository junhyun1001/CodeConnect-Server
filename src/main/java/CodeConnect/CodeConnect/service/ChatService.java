package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatRoomResponseDto;
import CodeConnect.CodeConnect.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public ResponseDto<ChatRoomResponseDto> createOrGetChatRoom(Recruitment recruitment) {

        ChatRoom chatRoom = recruitment.getChatRoom();
        if (chatRoom != null) {
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom);
            return ResponseDto.setSuccess("채팅방이 이미 존재합니다.", chatRoomResponseDto);
        } else {
            chatRoom = new ChatRoom(recruitment);
            chatRoomRepository.save(chatRoom);
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(chatRoom);
            return ResponseDto.setSuccess("채팅방이 생성되었습니다.", chatRoomResponseDto);
        }

    }

}
