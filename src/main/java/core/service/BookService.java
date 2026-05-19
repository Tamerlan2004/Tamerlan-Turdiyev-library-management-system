package core.service;

import core.dto.BookDTO;
import core.entity.Author;
import core.entity.Book;
import core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    @Transactional
    public Book create(BookDTO dto) {
        Author author = authorService.findById(dto.getAuthorId());

        Book book = Book.builder()
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .publicationYear(dto.getPublicationYear())
                .author(author)
                .available(dto.getAvailable() != null ? dto.getAvailable() : true)
                .build();

        log.info("Создана книга: {}", dto.getTitle());
        return bookRepository.save(book);
    }

    public Page<Book> findAllWithFilterAndPagination(
            String title,
            String isbn,
            Integer publicationYear,
            Long authorId,
            Boolean available,
            String titleContains,
            Pageable pageable) {

        if (titleContains != null && !titleContains.isBlank()) {
            return bookRepository.findByTitleContainingIgnoreCase(titleContains, pageable);
        }
        if (title != null && !title.isBlank()) {
            return bookRepository.findByTitle(title, pageable);
        }
        if (isbn != null && !isbn.isBlank()) {
            return bookRepository.findByIsbn(isbn, pageable);
        }
        if (publicationYear != null) {
            return bookRepository.findByPublicationYear(publicationYear, pageable);
        }
        if (authorId != null) {
            return bookRepository.findByAuthorId(authorId, pageable);
        }
        if (available != null) {
            return bookRepository.findByAvailable(available, pageable);
        }

        return bookRepository.findAll(pageable);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + id));
    }

    @Transactional
    public Book update(Long id, BookDTO dto) {
        Book existing = findById(id);

        existing.setTitle(dto.getTitle());
        existing.setIsbn(dto.getIsbn());
        existing.setPublicationYear(dto.getPublicationYear());
        existing.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : existing.isAvailable());

        if (dto.getAuthorId() != null) {
            Author author = authorService.findById(dto.getAuthorId());
            existing.setAuthor(author);
        }

        log.info("Обновлена книга: {}", existing.getTitle());
        return bookRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Книга не найдена с id: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Удалена книга с id: {}", id);
    }

    @Transactional
    public void updateCoverImage(Long bookId, String coverImagePath) {
        Book book = findById(bookId);
        book.setCoverImagePath(coverImagePath);
        bookRepository.save(book);
        log.info("Обложка обновлена для книги id={}. Путь: {}", bookId, coverImagePath);
    }

    @Transactional
    public void deleteCoverImage(Long bookId) {
        Book book = findById(bookId);
        String oldPath = book.getCoverImagePath();
        book.setCoverImagePath(null);
        bookRepository.save(book);

        log.info("Обложка удалена для книги id={}. Старый путь: {}", bookId, oldPath);
    }
}
