package br.dev.diisk.infra.jpas.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.diisk.domain.entities.expense.FixedExpense;

public interface FixedExpenseJPA extends JpaRepository<FixedExpense, Long> {

    @Query("""
            SELECT fe FROM FixedExpense fe
            JOIN FETCH fe.category
            WHERE 1=1
            AND fe.user.id = :userId
            AND (
                :categoryId IS NULL OR fe.category.id = :categoryId
            )
            AND (:active IS NULL OR (
                (
                    :active = TRUE
                    AND (fe.endReference IS NULL OR fe.endReference > CURRENT_TIMESTAMP)
                    AND (fe.startReference < CURRENT_TIMESTAMP)
                ) OR
                (
                    :active = FALSE AND (
                        fe.endReference <= CURRENT_TIMESTAMP
                        OR fe.startReference >= CURRENT_TIMESTAMP
                    )
                )
            ))
            AND fe.deleted = FALSE
            """)
    Page<FixedExpense> findAllWithFilter(
            Long userId,
            Long categoryId,
            Boolean active,
            Pageable pageable);

}
