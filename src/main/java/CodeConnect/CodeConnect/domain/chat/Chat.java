package CodeConnect.CodeConnect.domain.chat;

import CodeConnect.CodeConnect.dto.chat.ChatDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Chat")
@Getter
@Setter
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId; // 메시지 id

    private String nickname; // 발신자

    private String message; // 메시지

    private String currentDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public Chat(ChatDto chatDto) {
        this.nickname = chatDto.getNickname();
        this.message = chatDto.getMessage();
        this.currentDateTime = chatDto.getCurrentDateTime();
    }

    // 연관관계 메소드
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.getChatList().add(this);
    }

}