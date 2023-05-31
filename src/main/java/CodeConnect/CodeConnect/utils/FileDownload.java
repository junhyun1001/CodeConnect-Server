package CodeConnect.CodeConnect.utils;

import CodeConnect.CodeConnect.dto.file.FileRequestDto;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class FileDownload {

    public static void downloadFile(HttpServletResponse response, FileRequestDto fileRequestDto) {
        // 직접 파일 정보를 변수에 저장해 놨지만, 이 부분이 db에서 읽어왔다고 가정한다.
        String filePath = fileRequestDto.getFilePath();
        int lastIndexOfSlash = filePath.lastIndexOf("/");
        String fileName = filePath.substring(lastIndexOfSlash + 1);
        String contentType = fileRequestDto.getFileContentType();
        File file = new File(filePath);
        long fileLength = file.length();
        //파일의 크기와 같지 않을 경우 프로그램이 멈추지 않고 계속 실행되거나, 잘못된 정보가 다운로드 될 수 있다.

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", "" + fileLength);
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        try (
                FileInputStream fis = new FileInputStream(filePath);
                OutputStream out = response.getOutputStream();
        ) {
            int readCount = 0;
            byte[] buffer = new byte[1024];
            while ((readCount = fis.read(buffer)) != -1) {
                out.write(buffer, 0, readCount);
            }
        } catch (Exception ex) {
            throw new RuntimeException("file Save Error");
        }
    }

}
