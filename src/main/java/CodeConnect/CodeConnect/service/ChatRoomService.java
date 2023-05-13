package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.converter.EntityToDto;
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
        List<ChatRoom> chatRoomList = chatRoomRepository.findByCurrentParticipantMemberListContaining(email);

        List<ChatRoomDto> chatRoomDtoList = EntityToDto.mapListToDto(chatRoomList, ChatRoomDto::new);

        return ResponseDto.setSuccess("가입한 모집 목록", chatRoomDtoList);
    }


    public ResponseDto<ChatRoomDto> createOrGetChatRoom(Recruitment recruitment) {

        ChatRoom chatRoom = recruitment.getChatRoom();

        if (chatRoom == null) {
            chatRoom = new ChatRoom(recruitment);
            chatRoomRepository.save(chatRoom);
        }

        String resultMessage = (chatRoom == recruitment.getChatRoom()) ? "이미 존재하는 채팅방 입니다." : "채팅방이 생성되었습니다.";
        log.info("************************* " + resultMessage + ": {}, {} *************************", chatRoom.getRoomId(), chatRoom.getTitle());

        return ResponseDto.setSuccess(resultMessage, new ChatRoomDto(chatRoom));

    }

}
