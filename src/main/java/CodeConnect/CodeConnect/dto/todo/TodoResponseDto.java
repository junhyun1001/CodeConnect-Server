package CodeConnect.CodeConnect.dto.todo;

import CodeConnect.CodeConnect.domain.todo.Todo;
import lombok.Getter;

@Getter
public class TodoResponseDto {

    private final Long todoId;

    private final String content;

    private final boolean isCompleted;

    public TodoResponseDto(Todo todo) {
        this.todoId = todo.getTodoId();
        this.content = todo.getContent();
        this.isCompleted = todo.isCompleted();
    }

}
