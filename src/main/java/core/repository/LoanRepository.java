package core.repository;

import core.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMemberId(Long memberId);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByReturned(Boolean returned);

    List<Loan> findByMemberIdAndReturned(Long memberId, Boolean returned);

    List<Loan> findByBookIdAndReturned(Long bookId, Boolean returned);

    boolean existsByBookIdAndReturnedFalse(Long bookId);
}