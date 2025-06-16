package br.dev.diisk.infra.transaction.income;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;

public interface IncomeTransactionJPA extends JpaRepository<IncomeTransaction, Long> {

    @Query("""
            SELECT t FROM IncomeTransaction t
            WHERE t.user.id = :userId
            AND t.date IS NULL OR (
                (:startDate IS NULL OR t.date >= :startDate)
                AND (:endDate IS NULL OR t.date <= :endDate)
            )
            AND (
                :searchString IS NULL
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(t.value AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(t.date AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(t.category.name) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(t.category.description) LIKE LOWER(CONCAT('%', :searchString, '%'))
                OR LOWER(CAST(t.category.type AS string)) LIKE LOWER(CONCAT('%', :searchString, '%'))
            )
            AND t.deleted = false
            ORDER BY 
                CASE WHEN t.date IS NULL THEN 0 ELSE 1 END,
                t.date DESC
            """)
    Page<IncomeTransaction> findAllBy(Long userId, LocalDateTime startDate, LocalDateTime endDate, String searchString,
            Pageable pageable);

}
