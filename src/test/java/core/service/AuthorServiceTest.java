package core.service;

import core.entity.Author;
import core.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void createAuthor_ShouldSaveAndReturnAuthor() {
        Author author = Author.builder().name("Лев Толстой").build();
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.create(author);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Лев Толстой");
    }
}