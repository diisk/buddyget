package br.dev.diisk.infra.finance.income_recurring;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;

public interface IncomeRecurringJPA extends JpaRepository<IncomeRecurring, Long> {

    @Query("""
            SELECT r FROM IncomeRecurring r
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
    Page<IncomeRecurring> findAllBy(Long userId, String searchString, Pageable pageable);

    List<IncomeRecurring> findAllByUser_IdAndActiveTrueAndDeletedFalse(Long userId);

}
