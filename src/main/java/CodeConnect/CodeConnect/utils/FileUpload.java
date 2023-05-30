package CodeConnect.CodeConnect.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileUpload {


    public static String fileUpload(MultipartFile file) {
        final String uploadPath = "src/main/resources/image";

        String fileRealName = file.getOriginalFilename(); //파일명을 얻어낼 수 있는 메서드!
        String fileExtension = null;

        // 서버에 저장할 파일이름 fileextension으로 .jpg이런식의  확장자 명을 구함
        if (fileRealName != null) {
            fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."));
        }

		/*
		  파일 업로드시 파일명이 동일한 파일이 이미 존재할 수도 있고 사용자가
		  업로드 하는 파일명이 언어 이외의 언어로 되어있을 수 있습니다.
		  타인어를 지원하지 않는 환경에서는 정산 동작이 되지 않습니다.(리눅스가 대표적인 예시)
		  고유한 랜던 문자를 통해 db와 서버에 저장할 파일명을 새롭게 만들어 준다.
		 */

        UUID fileName = UUID.randomUUID(); // 랜덤 파일 이름 생성
        File saveFile = new File(uploadPath + "chat/file/" + fileName + fileExtension);  // 적용 후
        try {
            file.transferTo(saveFile); // 실제 파일 저장메서드(filewriter 작업을 손쉽게 한방에 처리해준다.)
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return saveFile.getPath();
    }

}
