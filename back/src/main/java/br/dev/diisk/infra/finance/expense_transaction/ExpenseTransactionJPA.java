package br.dev.diisk.infra.finance.expense_transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;

public interface ExpenseTransactionJPA extends JpaRepository<ExpenseTransaction, Long> {

        List<ExpenseTransaction> findAllByUser_IdAndDateIsNullAndDeletedFalse(Long userId);

        @Query("""
                        SELECT t FROM ExpenseTransaction t
                        WHERE t.user.id = :userId
                        AND t.date IS NOT NULL
                        AND (:startDate IS NULL OR t.date >= :startDate)
                        AND (:endDate IS NULL OR t.date <= :endDate)
                        AND (
                            :searchString IS NULL
                            OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(CAST(t.value AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(CAST(t.date AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(CAST(t.dueDate AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(t.category.name) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(t.category.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                            OR LOWER(CAST(t.category.type AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                        )
                        AND t.deleted = false
                        ORDER BY t.date DESC
                        """)
        Page<ExpenseTransaction> findAllPaidBy(Long userId, LocalDateTime startDate, LocalDateTime endDate,
                        String searchString,
                        Pageable pageable);

        List<ExpenseTransaction> findAllByExpenseRecurring_IdInAndDeletedFalse(List<Long> recurringIds);

}
