package CodeConnect.CodeConnect.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ChatDto {

    private Long roomId;

    private String nickname;

    private String message;

    private String currentDateTime = changeDateTimeFormat(LocalDateTime.now());

    public String changeDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        return dateTime.format(formatter);
    }

}
