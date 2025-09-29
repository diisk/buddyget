package br.dev.diisk.infra.finance.income_transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;

public interface IncomeTransactionJPA extends JpaRepository<IncomeTransaction, Long> {

        @Query("""
                        SELECT t FROM IncomeTransaction t
                        WHERE t.user.id = :userId
                        AND t.paymentDate IS NOT NULL
                        AND (:startDate IS NULL OR t.paymentDate >= :startDate)
                        AND (:endDate IS NULL OR t.paymentDate <= :endDate)
                        AND (
                            :searchString IS NULL
                            OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(CAST(t.value AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(CAST(t.paymentDate AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(t.category.name) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(t.category.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(CAST(t.category.type AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                        )
                        AND t.deleted = false
                        ORDER BY t.paymentDate DESC
                        """)
        Page<IncomeTransaction> findAllPaidBy(Long userId, LocalDateTime startDate, LocalDateTime endDate,
                        String searchString,
                        Pageable pageable);

        List<IncomeTransaction>  findAllByIncomeRecurring_IdInAndDeletedFalse(List<Long> recurringIds);

        // Boolean existsByIncomeRecurring_IdAndRecurringReferenceDateNotNullAndRecurringReferenceDateGreaterThanEqualAndRecurringReferenceDateLessThanAndDeletedFalse(
        //                 Long incomeRecurringId, LocalDateTime startDate, LocalDateTime endDate);

        List<IncomeTransaction> findAllByUser_IdAndPaymentDateIsNullAndDeletedFalse(Long userId);

}
