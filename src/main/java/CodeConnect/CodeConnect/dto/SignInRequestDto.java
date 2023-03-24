package CodeConnect.CodeConnect.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SignInRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
