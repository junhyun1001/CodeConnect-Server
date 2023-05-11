package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public ResponseDto<String> handleInvalidDataAccessApiUsageException() {
        return ResponseDto.setFail("올바르지 않은 토큰입니다.");
    }

}
