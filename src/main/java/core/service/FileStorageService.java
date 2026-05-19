package core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String saveFile(MultipartFile file, Long bookId) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Файл пустой");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";

        String newFilename = "book_" + bookId + "_" + UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(newFilename);

        file.transferTo(filePath.toFile());

        log.info("Файл сохранён: {} для книги id={}", newFilename, bookId);
        return newFilename;
    }

    public Path getFilePath(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }

    public void deleteFile(String filename) {
        try {
            Path filePath = getFilePath(filename);
            Files.deleteIfExists(filePath);
            log.info("Удалён файл: {}", filename);
        } catch (IOException e) {
            log.warn("Не удалось удалить файл: {}", filename);
        }
    }
}