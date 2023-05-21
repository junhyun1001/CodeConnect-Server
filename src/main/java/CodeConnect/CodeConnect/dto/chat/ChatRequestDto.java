package CodeConnect.CodeConnect.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDto {

    private Long roomId; // 방 id

    private String nickname; // 발신자

    private String message; // 메시지

}
