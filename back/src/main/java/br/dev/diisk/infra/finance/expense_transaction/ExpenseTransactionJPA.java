package br.dev.diisk.infra.finance.expense_transaction;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;

public interface ExpenseTransactionJPA extends JpaRepository<ExpenseTransaction, Long> {

    @Query("""
            SELECT t FROM ExpenseTransaction t
            WHERE t.user.id = :userId
            AND (
                t.date IS NULL OR (
                    (:startDate IS NULL OR t.date >= :startDate)
                    AND (:endDate IS NULL OR t.date <= :endDate)
                )
            )
            AND (
                :searchString IS NULL
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(t.value AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(t.date AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(t.dueDate AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(t.category.name) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(t.category.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(t.category.type AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(
                    CASE 
                        WHEN t.date IS NOT NULL THEN 'Pago'
                        WHEN t.dueDate IS NOT NULL AND t.dueDate < CURRENT_TIMESTAMP THEN 'Atrasado'
                        ELSE 'Pendente'
                    END
                ) LIKE LOWER(CONCAT('%', :searchString, '%'))
            )
            AND t.deleted = false
            ORDER BY
                CASE WHEN t.date IS NULL THEN 0 ELSE 1 END,
                t.date DESC
            """)
    Page<ExpenseTransaction> findAllBy(Long userId, LocalDateTime startDate, LocalDateTime endDate, String searchString,
            Pageable pageable);

}
