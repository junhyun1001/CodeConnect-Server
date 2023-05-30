package CodeConnect.CodeConnect.dto.todo;

import lombok.Getter;

@Getter
public class DeleteTodoResponseDto {

    private final Long todoId;

    private final Boolean result;

    public DeleteTodoResponseDto(Long todoId, Boolean result) {
        this.todoId = todoId;
        this.result = result;
    }
}
