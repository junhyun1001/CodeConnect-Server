package CodeConnect.CodeConnect.dto.chat;

import CodeConnect.CodeConnect.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRequestDto {

    private Long roomId; // 방 id

    private String nickname; // 발신자

    private String message; // 메시지

    private String currentDateTime = TimeUtils.changeChatTimeFormat((LocalDateTime.now())); // 메시지 보낸 시간

    private String profileImagePath; // 회원 프로필 사진

}
