package core.service;

import core.dto.BookDTO;
import core.entity.Author;
import core.entity.Book;
import core.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBook_ShouldSaveBookSuccessfully() {
        // Given
        Author author = Author.builder().id(1L).name("Джордж Оруэлл").build();
        BookDTO dto = BookDTO.builder()
                .title("1984")
                .isbn("978-0451524935")
                .publicationYear(1949)
                .authorId(1L)
                .build();

        when(authorService.findById(1L)).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Book result = bookService.create(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("1984");
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.isAvailable()).isTrue();
    }
}