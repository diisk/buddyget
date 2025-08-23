package br.dev.diisk.infra.finance.expense_recurring;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;

public interface ExpenseRecurringJPA extends JpaRepository<ExpenseRecurring, Long> {

    @Query("""
            SELECT r FROM ExpenseRecurring r
            WHERE r.user.id = :userId
            AND (
                :searchString IS NULL
                OR LOWER(r.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(r.value AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(r.period.startDate AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(r.period.endDate AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(r.category.name) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(r.category.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(r.category.type AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
            )
            AND r.deleted = false
            ORDER BY
                r.createdAt DESC
            """)
    Page<ExpenseRecurring> findAllBy(Long userId, String searchString, Pageable pageable);

    List<ExpenseRecurring> findAllByUser_IdAndActiveTrueAndDeletedFalse(Long userId);
}
