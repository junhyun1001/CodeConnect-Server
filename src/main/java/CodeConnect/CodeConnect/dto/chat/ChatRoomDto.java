package CodeConnect.CodeConnect.dto.chat;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChatRoomDto {

    private final Long roomId;

    private final String title;

    private final int currentCount;

    private final String hostNickname;

    private final String currentDateTime;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.title = chatRoom.getTitle();
        this.currentCount = chatRoom.getCurrentCount();
        this.hostNickname = chatRoom.getHostNickname();
        this.currentDateTime = chatRoom.getCurrentDateTime();
    }

}
