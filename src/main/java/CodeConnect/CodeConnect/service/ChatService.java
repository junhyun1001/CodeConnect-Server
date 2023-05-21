package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.chat.Chat;
import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.dto.chat.ChatDto;
import CodeConnect.CodeConnect.repository.ChatRepository;
import CodeConnect.CodeConnect.repository.ChatRoomRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    public void saveChat(ChatDto chatDto) {
        ChatRoom chatRoom = validateExistChatRoom(chatDto.getRoomId());

        Chat chat = new Chat(chatDto);
        chatRoom.setChatList(chat);

        chatRepository.save(chat);
    }

    // 해당 채팅방 존재 여부 확인
    public ChatRoom validateExistChatRoom(Long id) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);
        return optionalChatRoom.orElse(null);
    }


}
