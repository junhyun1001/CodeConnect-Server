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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

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

    @Transactional(readOnly = true)
    public ResponseDto<ChatRoomDto> getChatRoom(String email, Long id) {

        memberService.validateExistMember(email);

        ChatRoom chatRoom = validateExistChatRoom(id);

        return ResponseDto.setSuccess("채팅방 조회", new ChatRoomDto(chatRoom));

    }

    @Transactional(readOnly = true)
    public ResponseDto<List<ChatRoomDto>> getChatRoomList(String email) {

        List<ChatRoom> chatRoomList = chatRoomRepository.findByCurrentParticipantMemberListContaining(email);

        List<ChatRoomDto> chatRoomDtoList = EntityToDto.mapListToDto(chatRoomList, ChatRoomDto::new);

        return ResponseDto.setSuccess("참여한 채팅 목록", chatRoomDtoList);

    }

    public void leaveChatRoom(String email, Long id) {

        ChatRoom chatRoom = validateExistChatRoom(id);

        chatRoom.getCurrentParticipantMemberList().remove(email);

        chatRoomRepository.save(chatRoom);

    }


    // 해당 게시글 존재 여부 확인
    public ChatRoom validateExistChatRoom(Long id) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);
        return optionalChatRoom.orElse(null);
    }

}
