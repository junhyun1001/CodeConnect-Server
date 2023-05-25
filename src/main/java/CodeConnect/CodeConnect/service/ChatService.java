package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.chat.Chat;
import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.chat.ChatRequestDto;
import CodeConnect.CodeConnect.repository.ChatRepository;
import CodeConnect.CodeConnect.repository.ChatRoomRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    public void saveChat(ChatRequestDto chatRequestDto) {

        ChatRoom chatRoom = validateExistChatRoom(chatRequestDto.getRoomId());

        Chat chat = new Chat(chatRequestDto);

        chatRoom.setChatList(chat);

        Member member = memberRepository.findByNickname(chat.getNickname());
        member.setChat(chat);

        chatRepository.save(chat);

        log.info("******************** {}번 방 메시지 저장 {}:{} ********************", chat.getChatRoom().getRoomId(), chat.getNickname(), chat.getMessage());

    }

    // 해당 채팅방 존재 여부 확인
    public ChatRoom validateExistChatRoom(Long id) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);
        return optionalChatRoom.orElse(null);
    }


}
