package CodeConnect.CodeConnect.dto.chat;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private final Long roomId;

    private final String title;

    private final String hostNickname;

    private final String currentDateTime;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.title = chatRoom.getTitle();
        this.hostNickname = chatRoom.getHostNickname();
        this.currentDateTime = chatRoom.getCurrentDateTime();
    }

}
