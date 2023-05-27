package CodeConnect.CodeConnect.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChatRequestDto {

    private Long roomId; // 방 id

    private String nickname; // 발신자

    private String message; // 메시지

    private String currentDateTime = changeDateTimeFormat(LocalDateTime.now()); // 메시지 보낸 시간

    private String profileImagePath; // 회원 프로필 사진

    public String changeDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

}
