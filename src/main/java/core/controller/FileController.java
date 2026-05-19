package core.controller;

import core.entity.Book;
import core.service.BookService;
import core.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;
    private final BookService bookService;

    @PostMapping("/books/{bookId}/cover")
    public ResponseEntity<String> uploadBookCover(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file) {

        try {
            String filename = fileStorageService.saveFile(file, bookId);
            bookService.updateCoverImage(bookId, filename);

            return ResponseEntity.ok("Обложка успешно загружена: " + filename);
        } catch (Exception e) {
            log.error("Ошибка загрузки файла", e);
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/books/{bookId}/cover")
    public ResponseEntity<Resource> getBookCover(@PathVariable Long bookId) {
        Book book = bookService.findById(bookId);

        if (book.getCoverImagePath() == null || book.getCoverImagePath().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = fileStorageService.getFilePath(book.getCoverImagePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + book.getCoverImagePath() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Ошибка при получении файла", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}