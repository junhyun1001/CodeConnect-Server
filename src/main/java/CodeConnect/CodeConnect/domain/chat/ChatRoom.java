package CodeConnect.CodeConnect.domain.chat;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import CodeConnect.CodeConnect.dto.chat.ChatDto;
import CodeConnect.CodeConnect.service.ChatService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Chat_Room")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId; // 방 고유 id

    private String title; // 방 제목

    private String hostNickname; // 방장

    private String currentDateTime; // 방 생성 시간

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    @JsonIgnore
    private Recruitment recruitment; // 게시글 정보

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chatList = new ArrayList<>();

    public ChatRoom(Recruitment recruitment) {
        this.recruitment = recruitment;
        this.title = recruitment.getTitle();
        this.hostNickname = recruitment.getNickname();
        this.currentDateTime = changeDateTimeFormat(LocalDateTime.now());
    }

    public String changeDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        return dateTime.format(formatter);
    }

}
