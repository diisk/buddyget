package br.dev.diisk.infra.budget;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.budget.Budget;

public interface BudgetJPA extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUser_IdAndCategory_Id(Long userId, Long categoryId);
    // Add any custom methods if needed
}
