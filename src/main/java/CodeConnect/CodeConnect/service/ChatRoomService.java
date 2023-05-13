package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatRoomDto;
import CodeConnect.CodeConnect.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ResponseDto<List<ChatRoomDto>> getChatRoomList(String email) {
        List<ChatRoom> byCurrentParticipantMemberList = chatRoomRepository.findByCurrentParticipantMemberListContaining(email);

        // Transform List<ChatRoom> to List<ChatRoomDto>
        List<ChatRoomDto> chatRoomDtoList = byCurrentParticipantMemberList.stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("가입한 모집 목록", chatRoomDtoList);
    }


    public ResponseDto<ChatRoomDto> createOrGetChatRoom(Recruitment recruitment) {

        ChatRoom chatRoom = recruitment.getChatRoom();
        if (chatRoom != null) {
            log.info("************************* 이미 존재하는 채팅방:{}, {} *************************", chatRoom.getRoomId(), chatRoom.getTitle());
            return ResponseDto.setSuccess("이미 존재하는 채팅방 입니다.", new ChatRoomDto(chatRoom));
        } else {
            chatRoom = new ChatRoom(recruitment);
            chatRoomRepository.save(chatRoom);
            log.info("************************* 채팅방 생성:{}, {} *************************", chatRoom.getRoomId(), chatRoom.getTitle());
            return ResponseDto.setSuccess("채팅방이 생성되었습니다.", new ChatRoomDto(chatRoom));
        }

    }

}
