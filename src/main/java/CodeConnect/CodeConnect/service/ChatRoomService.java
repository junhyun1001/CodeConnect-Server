package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.converter.EntityToDto;
import CodeConnect.CodeConnect.domain.chat.Chat;
import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatRoomDto;
import CodeConnect.CodeConnect.repository.ChatRepository;
import CodeConnect.CodeConnect.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomService {

    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

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
    public ResponseDto<Map<Object, Object>> getChatRoom(String email, Long id) {

        Member member = memberService.validateExistMember(email);
        String nickname = member.getNickname();

        ChatRoom chatRoom = validateExistChatRoom(id);

        List<Chat> byChatRoom = chatRepository.findByChatRoom(chatRoom);

        // dto 생성
        ChatRoomDto chatRoomDto = new ChatRoomDto(chatRoom);
        List<ChatResponseDto> chatResponseDtos = EntityToDto.mapListToDto(byChatRoom, ChatResponseDto::new);

        // return Map 생성
        Map<Object, Object> chatRoomChatMap = new HashMap<>();
        chatRoomChatMap.put("ROOM_INFO", chatRoomDto);
        chatRoomChatMap.put("CHAT", chatResponseDtos);
        chatRoomChatMap.put("MY_NICKNAME", nickname);

        return ResponseDto.setSuccess("채팅방 조회", chatRoomChatMap);

    }

    @Transactional(readOnly = true)
    public ResponseDto<List<ChatRoomDto>> getChatRoomList(String email) {

        List<ChatRoom> chatRoomList = chatRoomRepository.findByCurrentParticipantMemberListContaining(email);

        List<ChatRoomDto> chatRoomDtoList = EntityToDto.mapListToDto(chatRoomList, ChatRoomDto::new);

        return ResponseDto.setSuccess("참여한 채팅 목록", chatRoomDtoList);

    }

    // 해당 게시글 존재 여부 확인
    public ChatRoom validateExistChatRoom(Long id) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);
        return optionalChatRoom.orElse(null);
    }

}
