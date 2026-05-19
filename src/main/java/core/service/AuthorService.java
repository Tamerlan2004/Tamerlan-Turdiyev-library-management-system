package core.service;

import core.entity.Author;
import core.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public Author create(Author author) {
        log.info("Создание автора: {}", author.getName());
        return authorRepository.save(author);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public List<Author> findAllWithFilter(String name, String biographyContains) {
        if (name != null && !name.isBlank()) {
            return authorRepository.findByNameContainingIgnoreCase(name);
        }
        if (biographyContains != null && !biographyContains.isBlank()) {
            return authorRepository.findByBiographyContainingIgnoreCase(biographyContains);
        }
        return findAll();
    }

    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автор не найден с id: " + id));
    }

    @Transactional
    public Author update(Long id, Author updatedAuthor) {
        Author existing = findById(id);
        existing.setName(updatedAuthor.getName());
        existing.setBiography(updatedAuthor.getBiography());
        log.info("Обновлён автор: {}", existing.getName());
        return authorRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Автор не найден с id: " + id);
        }
        authorRepository.deleteById(id);
        log.info("Удалён автор с id: {}", id);
    }
}