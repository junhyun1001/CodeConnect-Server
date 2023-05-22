package CodeConnect.CodeConnect.dto.chat;

import CodeConnect.CodeConnect.domain.chat.Chat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

    private Long chatId; // 메시지 id

    private String nickname; // 발신자

    private String message; // 메시지

    private String currentDateTime; // 현재 시간

    public ChatResponseDto(Chat chat) {
        this.chatId = chat.getChatId();
        this.nickname = chat.getNickname();
        this.message = chat.getMessage();
        this.currentDateTime = chat.getCurrentDateTime();
    }

}
