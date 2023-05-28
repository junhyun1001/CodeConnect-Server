package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.converter.EntityToDto;
import CodeConnect.CodeConnect.domain.chat.Chat;
import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.domain.todo.Todo;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatRoomDto;
import CodeConnect.CodeConnect.dto.todo.TodoResponseDto;
import CodeConnect.CodeConnect.repository.ChatRepository;
import CodeConnect.CodeConnect.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomService {

    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    // 채팅방 생성
    public ResponseDto<ChatRoomDto> createOrGetChatRoom(Recruitment recruitment) {

        ChatRoom chatRoom = recruitment.getChatRoom();

        if (chatRoom == null) {
            chatRoom = new ChatRoom(recruitment);
            chatRoomRepository.save(chatRoom);
        }

        String resultMessage = (chatRoom == recruitment.getChatRoom()) ? "이미 존재하는 채팅방 입니다." : "채팅방이 생성되었습니다.";
        log.info("************************* " + resultMessage + ": {}, {} *************************", chatRoom.getRoomId(), chatRoom.getTitle());

        // dto 생성
        ChatRoomDto chatRoomDto = new ChatRoomDto(chatRoom);
        chatRoomDto.setCurrentCount(recruitment.getCurrentCount()); // 작성자 인원 제외하고 인원수 리턴

        return ResponseDto.setSuccess(resultMessage, chatRoomDto);

    }

    // 채팅방 단일 조회
    @Transactional(readOnly = true)
    public ResponseDto<Map<String, Object>> getChatRoom(String email, Long id) {

        Member member = memberService.validateExistMember(email);
        String nickname = member.getNickname();

        ChatRoom chatRoom = validateExistChatRoom(id);

        List<Chat> byChatRoom = chatRepository.findByChatRoom(chatRoom);

        // {닉네임: 사진} 형식의 맵 생성
        List<String> currentParticipantMemberList = chatRoom.getCurrentParticipantMemberList();
        Map<String, String> nicknameAndProfileImageMap = new HashMap<>();
        for (String memberEmail : currentParticipantMemberList) {
            member = memberService.validateExistMember(memberEmail);
            nicknameAndProfileImageMap.put(member.getNickname(), member.getProfileImagePath());
        }

        // ChatRoomDto 생성
        ChatRoomDto chatRoomDto = new ChatRoomDto(chatRoom);
        List<ChatResponseDto> chatResponseDtos = EntityToDto.mapListToDto(byChatRoom, ChatResponseDto::new);

        // todoList dto 생성
        List<Todo> todo = chatRoom.getTodo();
        List<TodoResponseDto> todoResponseDtos = EntityToDto.mapListToDto(todo, TodoResponseDto::new);

        // return Map 생성
        Map<String, Object> chatRoomChatMap = new HashMap<>();
        chatRoomChatMap.put("ROOM_INFO", chatRoomDto);
        chatRoomChatMap.put("CHAT", chatResponseDtos);
        chatRoomChatMap.put("MY_NICKNAME", nickname);
        chatRoomChatMap.put("NICKNAME_IMAGE", nicknameAndProfileImageMap);
        chatRoomChatMap.put("TODO_LIST", todoResponseDtos);


        return ResponseDto.setSuccess("채팅방 조회", chatRoomChatMap);

    }

    // 가입한 모든 채팅방 목록
    @Transactional(readOnly = true)
    public ResponseDto<List<ChatRoomDto>> getChatRoomList(String email) {

        List<ChatRoom> chatRoomList = chatRoomRepository.findByCurrentParticipantMemberListContaining(email);

        List<ChatRoomDto> chatRoomDtoList = EntityToDto.mapListToDto(chatRoomList, ChatRoomDto::new);

        return ResponseDto.setSuccess("참여한 채팅 목록", chatRoomDtoList);

    }

    // 채팅방 나가기
    public ResponseDto<?> leaveChatRoom(String email, Long id) {

        ChatRoom chatRoom = validateExistChatRoom(id);

        chatRoom.getCurrentParticipantMemberList().remove(email);

        chatRoomRepository.save(chatRoom);

        log.info("******************** {}회원이 {}번 채팅방을 나갔습니다.", email, id);

        return ResponseDto.setSuccess("채팅방을 나갔습니다.", null);
    }

    // 해당 게시글 존재 여부 확인
    public ChatRoom validateExistChatRoom(Long id) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);
        return optionalChatRoom.orElse(null);
    }

}
