package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.todo.CreateTodoRequestDto;
import CodeConnect.CodeConnect.dto.todo.DeleteTodoRequestDto;
import CodeConnect.CodeConnect.dto.todo.TodoResponseDto;
import CodeConnect.CodeConnect.dto.todo.UpdateTodoRequestDto;
import CodeConnect.CodeConnect.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final SimpMessagingTemplate template;
    private final TodoService todoService;

    @MessageMapping("/todo/create")
    public void createTodo(CreateTodoRequestDto createTodoRequestDto) {
        TodoResponseDto todoResponseDto = todoService.createTodo(createTodoRequestDto);
        template.convertAndSend("/sub/todo/room/" + createTodoRequestDto.getRoomId(), todoResponseDto);
    }

    @MessageMapping("/todo/update")
    public void updateTodo(UpdateTodoRequestDto updateTodoRequestDto) {
        TodoResponseDto todoResponseDto = todoService.updateTodo(updateTodoRequestDto);
        template.convertAndSend("/sub/todo/room/" + updateTodoRequestDto.getRoomId(), todoResponseDto);
    }

    @MessageMapping("/todo/delete")
    public void deleteTodo(DeleteTodoRequestDto deleteTodoRequestDto) {
        boolean result = todoService.deleteTodo(deleteTodoRequestDto);
        template.convertAndSend("/sub/todo/room/" + deleteTodoRequestDto.getRoomId(), result);
    }


}
