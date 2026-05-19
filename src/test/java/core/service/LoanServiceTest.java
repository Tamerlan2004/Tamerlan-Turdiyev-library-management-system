package core.service;

import core.dto.LoanDTO;
import core.entity.Book;
import core.entity.Loan;
import core.entity.Member;
import core.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private BookService bookService;

    @InjectMocks
    private LoanService loanService;

    @Test
    void createLoan_ShouldCreateLoanAndMarkBookAsUnavailable() {
        // Given
        Member member = Member.builder().id(1L).fullName("Иван Иванов").build();
        Book book = Book.builder().id(10L).title("1984").available(true).build();
        LoanDTO dto = LoanDTO.builder()
                .memberId(1L)
                .bookId(10L)
                .loanDate(LocalDate.now())
                .build();

        when(memberService.findById(1L)).thenReturn(member);
        when(bookService.findById(10L)).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Loan result = loanService.create(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getBook()).isEqualTo(book);
        assertThat(book.isAvailable()).isFalse();
    }
}