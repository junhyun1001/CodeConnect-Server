package CodeConnect.CodeConnect.dto.todo;

import lombok.Getter;

@Getter
public class UpdateTodoRequestDto {

    private Long todoId;

    private String content;

    private Boolean isCompleted;

}
