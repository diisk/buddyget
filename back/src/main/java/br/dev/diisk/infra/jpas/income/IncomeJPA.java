package br.dev.diisk.infra.jpas.income;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.entities.income.Income;

public interface IncomeJPA extends JpaRepository<Income, Long> {

    @Query("""
            SELECT i FROM Income i
            JOIN FETCH i.category
            WHERE 1=1
            AND i.user.id = :userId
            AND i.receiptDate >= :startReference
            AND i.receiptDate <= :endReference
            AND (
                :categoryId IS NULL
                OR i.category.id = :categoryId
            )
            AND i.deleted = FALSE
            """)
    Page<Income> findReceipts(
            Long userId,
            LocalDateTime startReference,
            LocalDateTime endReference,
            Long categoryId,
            Pageable pageable);

    @Query("""
            SELECT i FROM Income i
            JOIN FETCH i.category
            WHERE 1=1
            AND i.user.id = :userId
            AND i.receiptDate IS NULL
            AND (
                :categoryId IS NULL
                OR i.category.id = :categoryId
            )
            AND i.deleted = FALSE
            """)
    Page<Income> findPendings(
            Long userId,
            Long categoryId,
            Pageable pageable);

}
