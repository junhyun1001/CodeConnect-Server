package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.chat.Chat;
import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByChatRoom(ChatRoom chatRoom);

}
