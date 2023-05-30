package CodeConnect.CodeConnect.dto.file;

import CodeConnect.CodeConnect.utils.BytesToMbConverter;
import lombok.Getter;

@Getter
public class FileResponseDto {

    private final String filePath;

    private final String fileSize;

    private final String fileContentType;

    public FileResponseDto(String filePath, Long fileSize, String fileContentType) {
        this.filePath = filePath;
        this.fileSize = BytesToMbConverter.bytesToMegabytes(fileSize);
        this.fileContentType = fileContentType;
    }

}
