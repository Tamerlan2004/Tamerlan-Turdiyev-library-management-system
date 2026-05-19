package core.service;

import core.dto.LoanDTO;
import core.entity.Book;
import core.entity.Loan;
import core.entity.Member;
import core.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final MemberService memberService;
    private final BookService bookService;
    private final EmailService emailService;   // ← Добавлено

    @Transactional
    public Loan create(LoanDTO dto) {
        Member member = memberService.findById(dto.getMemberId());
        Book book = bookService.findById(dto.getBookId());

        if (loanRepository.existsByBookIdAndReturnedFalse(dto.getBookId())) {
            throw new RuntimeException("Книга уже выдана и не возвращена");
        }

        if (!book.isAvailable()) {
            throw new RuntimeException("Книга помечена как недоступная");
        }

        Loan loan = Loan.builder()
                .member(member)
                .book(book)
                .loanDate(dto.getLoanDate() != null ? dto.getLoanDate() : java.time.LocalDate.now())
                .returnDate(dto.getReturnDate())
                .returned(dto.getReturned() != null ? dto.getReturned() : false)
                .build();

        book.setAvailable(false);

        Loan savedLoan = loanRepository.save(loan);

        emailService.sendLoanNotification(
                member.getEmail(),
                member.getFullName(),
                book.getTitle()
        );

        log.info("Книга выдана. Member: {}, Book: {}", member.getFullName(), book.getTitle());
        return savedLoan;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public List<Loan> findAllWithFilter(Long memberId, Long bookId, Boolean returned) {
        if (memberId != null && returned != null) {
            return loanRepository.findByMemberIdAndReturned(memberId, returned);
        }
        if (bookId != null && returned != null) {
            return loanRepository.findByBookIdAndReturned(bookId, returned);
        }
        if (memberId != null) {
            return loanRepository.findByMemberId(memberId);
        }
        if (bookId != null) {
            return loanRepository.findByBookId(bookId);
        }
        if (returned != null) {
            return loanRepository.findByReturned(returned);
        }
        return findAll();
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена с id: " + id));
    }

    @Transactional
    public Loan update(Long id, LoanDTO dto) {
        Loan existing = findById(id);

        boolean wasReturned = existing.isReturned();

        if (dto.getReturnDate() != null) {
            existing.setReturnDate(dto.getReturnDate());
        }
        if (dto.getReturned() != null) {
            existing.setReturned(dto.getReturned());

            if (!wasReturned && dto.getReturned()) {
                existing.getBook().setAvailable(true);

                emailService.sendBookReturnNotification(
                        existing.getMember().getEmail(),
                        existing.getMember().getFullName(),
                        existing.getBook().getTitle()
                );
            }
        }

        log.info("Обновлена выдача id: {}", id);
        return loanRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Loan loan = findById(id);
        if (!loan.isReturned()) {
            loan.getBook().setAvailable(true);
        }
        loanRepository.deleteById(id);
        log.info("Удалена выдача с id: {}", id);
    }
}