package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.chat.ChatRequestDto;
import CodeConnect.CodeConnect.dto.chat.ChatResponseDto;
import CodeConnect.CodeConnect.dto.file.FileRequestDto;
import CodeConnect.CodeConnect.dto.file.FileResponseDto;
import CodeConnect.CodeConnect.service.ChatService;
import CodeConnect.CodeConnect.utils.FileDownload;
import CodeConnect.CodeConnect.utils.FileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate template; // 특정 Broker로 메세지 전달
    private final ChatService chatService;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/message"
    @MessageMapping("/chat/message")
    public void message(ChatRequestDto chatRequestDto) {
        ChatResponseDto chatResponseDto = chatService.saveChat(chatRequestDto);
        template.convertAndSend("/sub/chat/room/" + chatRequestDto.getRoomId(), chatResponseDto);
    }

    @PostMapping("/chat/file/upload")
    public void uploadFile(@RequestPart("file") MultipartFile file, @RequestPart("roomId") String roomId) {
        String filePath = FileUpload.fileUpload(file);
        Long fileSize = file.getSize();
        String fileContentType = file.getContentType();
        FileResponseDto fileResponseDto = new FileResponseDto(filePath, fileSize, fileContentType);
        template.convertAndSend("/sub/chat/room/" + roomId, fileResponseDto);
    }

    @GetMapping("/chat/file/download")
    public void downloadFile(@RequestBody FileRequestDto fileRequestDto, HttpServletResponse response) {
        FileDownload.downloadFile(response, fileRequestDto);
    }
}

