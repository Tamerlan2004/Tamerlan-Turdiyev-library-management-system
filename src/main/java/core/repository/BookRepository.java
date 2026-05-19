package core.repository;

import core.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByTitle(String title, Pageable pageable);
    Page<Book> findByIsbn(String isbn, Pageable pageable);
    Page<Book> findByPublicationYear(Integer publicationYear, Pageable pageable);
    Page<Book> findByAuthorId(Long authorId, Pageable pageable);
    Page<Book> findByAvailable(Boolean available, Pageable pageable);
}