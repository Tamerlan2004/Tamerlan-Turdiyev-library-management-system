package core.controller;

import core.dto.BookDTO;
import core.dto.LoanDTO;
import core.dto.MemberDTO;
import core.entity.Author;
import core.entity.Book;
import core.entity.Loan;
import core.entity.Member;
import core.service.AuthorService;
import core.service.BookService;
import core.service.LoanService;
import core.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Library Management", description = "Основные операции с библиотекой")
public class LibraryController {

    private final AuthorService authorService;
    private final BookService bookService;
    private final MemberService memberService;
    private final LoanService loanService;

    @Operation(summary = "Создать автора", description = "Создание нового автора")
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(author));
    }

    @Operation(summary = "Получить всех авторов с фильтрами")
    @GetMapping("/authors")
    public List<Author> getAllAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String biographyContains) {
        return authorService.findAllWithFilter(name, biographyContains);
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.findById(id));
    }

    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author author) {
        return ResponseEntity.ok(authorService.update(id, author));
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить книги с пагинацией, сортировкой, поиском и фильтрацией")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка запроса")
    })
    @GetMapping("/books")
    public Page<Book> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer publicationYear,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) String titleContains,

            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "title") Pageable pageable) {

        log.info("Запрос книг с параметрами: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return bookService.findAllWithFilterAndPagination(
                title, isbn, publicationYear, authorId, available, titleContains, pageable);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(bookDTO));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.update(id, bookDTO));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members")
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(memberDTO));
    }

    @GetMapping("/members")
    public List<Member> getAllMembers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String faculty,
            @RequestParam(required = false) String groupName) {
        return memberService.findAllWithFilter(fullName, email, faculty, groupName);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findById(id));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.update(id, memberDTO));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/loans")
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanDTO loanDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.create(loanDTO));
    }

    @GetMapping("/loans")
    public List<Loan> getAllLoans(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Boolean returned) {
        return loanService.findAllWithFilter(memberId, bookId, returned);
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @PutMapping("/loans/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @Valid @RequestBody LoanDTO loanDTO) {
        return ResponseEntity.ok(loanService.update(id, loanDTO));
    }

    @DeleteMapping("/loans/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}