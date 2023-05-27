package CodeConnect.CodeConnect.dto.todo;

import lombok.Getter;

@Getter
public class UpdateTodoRequestDto {

    private Long todoId;

    private Long roomId;

    private String content;

    private Boolean isCompleted;

}
