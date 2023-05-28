package CodeConnect.CodeConnect.dto.todo;

import lombok.Getter;

@Getter
public class CreateTodoRequestDto {

    private Long roomId; // 채팅방 id

    private String content; // 내용

}
