package CodeConnect.CodeConnect.dto.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileRequestDto {

    private String filePath;

    private String fileContentType;

}
