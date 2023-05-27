package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.todo.CreateTodoRequestDto;
import CodeConnect.CodeConnect.dto.todo.TodoResponseDto;
import CodeConnect.CodeConnect.dto.todo.RoomIdRequestDto;
import CodeConnect.CodeConnect.dto.todo.UpdateTodoRequestDto;
import CodeConnect.CodeConnect.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final SimpMessagingTemplate template;
    private final TodoService todoService;

//    @PostMapping("/create")
//    public ResponseDto<TodoResponseDto> createTodo(@RequestBody CreateTodoRequestDto createTodoRequestDto) {
//        return todoService.createTodo(createTodoRequestDto);
//    }
//
//    @GetMapping("/list")
//    public ResponseDto<List<TodoResponseDto>> getTodoList(@RequestBody RoomIdRequestDto roomIdRequestDto) {
//        return todoService.getTodoList(roomIdRequestDto);
//    }
//
//    @PutMapping("/update")
//    public ResponseDto<TodoResponseDto> updateTodo(@RequestBody UpdateTodoRequestDto updateTodoRequestDto) {
//        return todoService.updateTodo(updateTodoRequestDto);
//    }

    @MessageMapping("/todo/create")
    public void createTodo(CreateTodoRequestDto createTodoRequestDto) {
        TodoResponseDto todo = todoService.createTodo(createTodoRequestDto);
        template.convertAndSend("/sub/todo/room/" + createTodoRequestDto.getRoomId(), todo);
    }

    @MessageMapping("/todo/update")
    public void updateTodo(UpdateTodoRequestDto updateTodoRequestDto) {
        TodoResponseDto todoResponseDto = todoService.updateTodo(updateTodoRequestDto);
        template.convertAndSend("/sub/todo/room/" + updateTodoRequestDto.getRoomId(), todoResponseDto);
    }


}
