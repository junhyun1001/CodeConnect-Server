package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.domain.chat.Chat;
import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.chat.ChatRequestDto;
import CodeConnect.CodeConnect.dto.chat.ChatResponseDto;
import CodeConnect.CodeConnect.dto.file.FileResponseDto;
import CodeConnect.CodeConnect.repository.ChatRepository;
import CodeConnect.CodeConnect.repository.MemberRepository;
import CodeConnect.CodeConnect.utils.FileUpload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRepository chatRepository;

    public ChatResponseDto saveChat(ChatRequestDto chatRequestDto) {

        ChatRoom chatRoom = chatRoomService.validateExistChatRoom(chatRequestDto.getRoomId());

        Chat chat = new Chat(chatRequestDto);

        chatRoom.setChatList(chat);

        Member member = memberRepository.findByNickname(chat.getNickname());
        member.setChat(chat);

        chatRepository.save(chat);

        log.info("******************** {}번 방 메시지 저장 {}:{} ********************", chat.getChatRoom().getRoomId(), chat.getNickname(), chat.getMessage());

        return new ChatResponseDto(chat);
    }

    public ResponseDto<FileResponseDto> fileUpload(MultipartFile file) {
        String filePath = FileUpload.fileUpload(file);
        Long fileSize = file.getSize();
        String fileContentType = file.getContentType();

        FileResponseDto fileResponseDto = new FileResponseDto(filePath, fileSize, fileContentType);

        return ResponseDto.setSuccess("파일 업로드", fileResponseDto);
    }

}
