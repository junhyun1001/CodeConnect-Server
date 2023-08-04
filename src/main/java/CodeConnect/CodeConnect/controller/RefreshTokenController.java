package CodeConnect.CodeConnect.controller;

import CodeConnect.CodeConnect.dto.ResponseDto;
import CodeConnect.CodeConnect.dto.token.Token;
import CodeConnect.CodeConnect.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final TokenProvider tokenProvider;

    @PostMapping("/refresh-token")
    public ResponseDto<String> validateRefreshToken(@RequestBody Token token) {
        // refresh token이 만료되지 않았을 경우 access token이 리턴되고
        // refresh token이 만료됐을 경우 에러 메시지가 리턴된다.
        String result = tokenProvider.validateRefreshToken(token.getRefreshToken());
        if (result != null) {
            return ResponseDto.setSuccess("새로운 access token 발급", result);
        } else {
            return ResponseDto.setFail("만료된 refresh token 입니다. 다시 로그인 하세요.");
        }
    }
}
