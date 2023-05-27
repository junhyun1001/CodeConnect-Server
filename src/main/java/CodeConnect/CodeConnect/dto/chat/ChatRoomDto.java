package CodeConnect.CodeConnect.dto.chat;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private Long roomId;

    private String title;

    private int currentCount;

    private String hostNickname;

    private String currentDateTime;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.title = chatRoom.getTitle();
        this.currentCount = chatRoom.getCurrentCount();
        this.hostNickname = chatRoom.getHostNickname();
        this.currentDateTime = chatRoom.getCurrentDateTime();
    }

}
