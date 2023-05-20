package CodeConnect.CodeConnect.repository;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.dto.chat.ChatRoomDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByCurrentParticipantMemberListContaining(String email);

}
