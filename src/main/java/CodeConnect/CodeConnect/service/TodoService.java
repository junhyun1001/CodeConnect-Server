package CodeConnect.CodeConnect.service;

import CodeConnect.CodeConnect.converter.EntityToDto;
import CodeConnect.CodeConnect.domain.chat.ChatRoom;
import CodeConnect.CodeConnect.domain.todo.Todo;
import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.todo.RoomIdRequestDto;
import CodeConnect.CodeConnect.dto.todo.CreateTodoRequestDto;
import CodeConnect.CodeConnect.dto.todo.TodoResponseDto;
import CodeConnect.CodeConnect.dto.todo.UpdateTodoRequestDto;
import CodeConnect.CodeConnect.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final ChatRoomService chatRoomService;
    private final TodoRepository todoRepository;

    // todo 생성
    public ResponseDto<TodoResponseDto> createTodo(CreateTodoRequestDto createTodoRequestDto) {

        ChatRoom chatRoom = chatRoomService.validateExistChatRoom(createTodoRequestDto.getRoomId());

        Todo todo = new Todo(createTodoRequestDto, chatRoom);
        todoRepository.save(todo);

        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        log.info("{}번 채팅방 To-Do 생성", createTodoRequestDto.getRoomId());

        return ResponseDto.setSuccess("Todo 생성", todoResponseDto);
    }

    // todo 조회
    public ResponseDto<List<TodoResponseDto>> getTodoList(RoomIdRequestDto roomIdRequestDto) {

        ChatRoom chatRoom = chatRoomService.validateExistChatRoom(roomIdRequestDto.getRoomId());

        List<Todo> todo = chatRoom.getTodo();

        List<TodoResponseDto> todoResponseDtos = EntityToDto.mapListToDto(todo, TodoResponseDto::new);

        log.info("{}번 방 Todo 리스트 조회", roomIdRequestDto.getRoomId());

        return ResponseDto.setSuccess("Todo 리스트 조회", todoResponseDtos);

    }

    // todo 업데이트
    public ResponseDto<TodoResponseDto> updateTodo(UpdateTodoRequestDto updateTodoRequestDto) {
        Todo todo = validateExistTodo(updateTodoRequestDto.getTodoId());

        todo.updateTodo(updateTodoRequestDto);
        todoRepository.save(todo);

        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        log.info("{}번 Todo가 업데이트 되었습니다.", todo.getTodoId());

        return ResponseDto.setSuccess("Todo가 업데이트 되었습니다.", todoResponseDto);

    }

    // todo 존재여부 확인
    public Todo validateExistTodo(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        return optionalTodo.orElseThrow(() -> new NoSuchElementException("존재하지 않는 Todo 입니다: " + id));
    }


}
