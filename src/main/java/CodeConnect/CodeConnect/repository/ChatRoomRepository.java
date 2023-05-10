package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
