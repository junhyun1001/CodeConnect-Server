package CodeConnect.CodeConnect.domain.chat;

import CodeConnect.CodeConnect.dto.chat.ChatRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private String currentDateTime; // 현재 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public Chat(ChatRequestDto chatRequestDto) {
        this.nickname = chatRequestDto.getNickname();
        this.message = chatRequestDto.getMessage();
        this.currentDateTime = changeDateTimeFormat(LocalDateTime.now());
    }

    // 연관관계 메소드
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.getChatList().add(this);
    }

    public String changeDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

}