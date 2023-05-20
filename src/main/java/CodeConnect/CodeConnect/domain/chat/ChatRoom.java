package CodeConnect.CodeConnect.domain.chat;

import CodeConnect.CodeConnect.domain.post.Recruitment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    private int currentCount; // 참여인원 수

    private String hostNickname; // 방장

    private String currentDateTime; // 방 생성 시간

    @OneToOne(mappedBy = "chatRoom")
    private Recruitment recruitment; // 게시글 정보

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Chat> chatList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "room_id"))
    @JsonIgnore
    private List<String> currentParticipantMemberList;

    public ChatRoom(Recruitment recruitment) {
        this.title = recruitment.getTitle();
        this.hostNickname = recruitment.getNickname();
        this.currentDateTime = changeDateTimeFormat(LocalDateTime.now());
        this.currentParticipantMemberList = new ArrayList<>(recruitment.getCurrentParticipantMemberList());
        this.currentCount = recruitment.getCurrentCount();
    }

    public String changeDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        return dateTime.format(formatter);
    }

}
