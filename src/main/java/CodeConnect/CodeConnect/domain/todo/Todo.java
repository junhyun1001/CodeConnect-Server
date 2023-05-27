package CodeConnect.CodeConnect.domain.todo;

import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.dto.todo.CreateTodoRequestDto;
import CodeConnect.CodeConnect.dto.todo.UpdateTodoRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Todo")
@Getter
@NoArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private String content; // 내용

    private boolean isCompleted; // 완료 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private ChatRoom chatRoom;

    public Todo(CreateTodoRequestDto createTodoRequestDto, ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        this.content = createTodoRequestDto.getContent();
        this.isCompleted = false;
    }

    public void updateTodo(UpdateTodoRequestDto updateTodoRequestDto) {
        this.content = updateTodoRequestDto.getContent();
        this.isCompleted = updateTodoRequestDto.getIsCompleted();
    }

}
