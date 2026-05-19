package core.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private Long id;
    private Long memberId;
    private Long bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Boolean returned;
}