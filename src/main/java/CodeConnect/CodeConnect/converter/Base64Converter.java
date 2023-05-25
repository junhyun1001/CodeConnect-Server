package CodeConnect.CodeConnect.converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;


public class Base64Converter {

    public static String saveImageFromBase64(String type, String base64Image) {

        // 이미지 파일 저장 디렉토리 설정
        String uploadDir = "src/main/resources/image";
        if(type.equals("qna"))
            uploadDir += "/qna";
        else if(type.equals("member"))
            uploadDir += "/member/profile";
        String fileName = UUID.randomUUID() + ".png";
        String filePath = uploadDir + "/" + fileName;

        try {
            // 디렉토리 생성
            Files.createDirectories(Paths.get(uploadDir).toAbsolutePath().normalize());

            // base64 인코딩된 이미지 데이터 디코딩하여 파일로 저장
            byte[] imageBytes;
            try {
                imageBytes = Base64.getDecoder().decode(base64Image);
            } catch (IllegalArgumentException e) {
                // 잘못된 Base64 문자가 포함된 경우 예외 처리 방법을 선택하거나 오류 메시지 반환
                return null;
            }

            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, imageBytes);

            return filePath;
        } catch (
                IOException e) {
            // 예외 처리
            e.printStackTrace();
            // 파일 저장 실패 시 예외 처리 방법을 선택하거나 null 등을 반환
            return null;
        }
    }

    // 기존 이미지 삭제 메소드
    public static void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            // 이미지 파일 삭제 로직을 구현합니다.
            // 예시로서 imagePath를 파일 경로로 사용하여 파일을 삭제하는 방식을 보여드립니다.
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
    }

}
