package CodeConnect.CodeConnect.domain.chat;

import javax.persistence.*;

@Entity
@Table(name = "Chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메시지 id

    private String nickname; // 발신자

    private String message; // 메시지

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

}