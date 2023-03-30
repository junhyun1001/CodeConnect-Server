package CodeConnect.CodeConnect.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 클라이언트 응답에 대한 성공, 실패를 리턴해주는 클래스
 */

@Getter
@Setter
@AllArgsConstructor(staticName = "set") // set이란 이름으로 생성자 생성
public class ResponseDto<D> {

    private boolean result;
    private String message;
    private D data;

    // 성공 리턴
    public static <D> ResponseDto<D> setSuccess(String message, D data) {
        return ResponseDto.set(true, message, data);
    }

    // 실패 리턴
    public static <D> ResponseDto<D> setFail(String message) {
        return ResponseDto.set(false, message, null);
    }

}

