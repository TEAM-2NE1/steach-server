package com.twentyone.steachserver.util.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtil {
    public static String storeFile(MultipartFile file, String uploadDir) throws IOException {
        // 저장할 파일 경로
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String fileRealName = file.getOriginalFilename();

        // 사진 업로드 폴더가 존재하지 않으면 생성
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            log.info("파일 저장을 위한 디렉토리 생성 중 오류 발생");
            throw new IOException("디렉토리 생성 중 오류가 발생", e);
        }

        // 파일 저장
        Path targetLocation = uploadPath.resolve(fileRealName);
        try {
            file.transferTo(targetLocation.toFile());
        } catch (IllegalStateException | IOException e) {
            log.info("파일 저장 중 오류 발생");
            throw new IOException("파일 저장 중 오류가 발생", e);
        }

        return fileRealName;
    }
}
