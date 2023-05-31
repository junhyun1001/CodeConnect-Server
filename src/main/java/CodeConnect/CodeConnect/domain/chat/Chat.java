package CodeConnect.CodeConnect.domain.chat;

import CodeConnect.CodeConnect.dto.chat.MessageType;
import CodeConnect.CodeConnect.utils.TimeUtils;
import CodeConnect.CodeConnect.domain.member.Member;
import CodeConnect.CodeConnect.dto.chat.ChatRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Chat")
@Getter
@Setter
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId; // 메시지 id

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String nickname; // 발신자

    private String message; // 메시지

    private String currentDateTime; // 현재 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private ChatRoom chatRoom;

    public Chat(ChatRequestDto chatRequestDto) {
        this.nickname = chatRequestDto.getNickname();
        this.message = chatRequestDto.getMessage();
        this.currentDateTime = TimeUtils.changeChatTimeFormat(LocalDateTime.now());
        this.messageType = chatRequestDto.getMessageType();
    }

    // 연관관계 메소드
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.getChatList().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getChats().add(this);
    }

}