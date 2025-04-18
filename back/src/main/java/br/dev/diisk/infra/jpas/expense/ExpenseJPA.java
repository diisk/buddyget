package br.dev.diisk.infra.jpas.expense;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.dev.diisk.domain.entities.expense.Expense;

public interface ExpenseJPA extends JpaRepository<Expense, Long> {

    @Query("""
            SELECT e FROM Expense e
            JOIN FETCH e.category
            LEFT JOIN FETCH e.creditCard
            LEFT JOIN FETCH e.fixedExpense
            WHERE 1=1
            AND e.user.id = :userId

            AND (:startDueDate IS NULL OR (
                e.dueDate IS NOT NULL AND
                e.dueDate >= :startDueDate
            ))
            AND (:endDueDate IS NULL OR (
                e.dueDate IS NOT NULL AND
                e.dueDate <= :endDueDate
            ))

            AND (:startPaymentDate IS NULL OR (
                e.paymentDate IS NOT NULL AND
                e.paymentDate >= :startPaymentDate
            ))
            AND (:endPaymentDate IS NULL OR (
                e.paymentDate IS NOT NULL AND
                e.paymentDate <= :endPaymentDate
            ))

            AND (:categoryId IS NULL OR e.category.id = :categoryId)
            AND (:creditCardId IS NULL OR (
                e.creditCard IS NOT NULL AND
                e.creditCard.id = :creditCardId
            ))
            AND (:fixedExpenseId IS NULL OR (
                e.fixedExpense IS NOT NULL AND
                e.fixedExpense.id = :fixedExpenseId
            ))

            AND e.deleted = FALSE
            """)
    Page<Expense> findAllWithFilter(
            Long userId,
            LocalDateTime startDueDate,
            LocalDateTime endDueDate,
            LocalDateTime startPaymentDate,
            LocalDateTime endPaymentDate,
            Long categoryId,
            Long creditCardId,
            Long fixedExpenseId,
            Pageable pageable);

}
