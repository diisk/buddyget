package br.dev.diisk.infra.budget;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.budget.Budget;

public interface BudgetJPA extends JpaRepository<Budget, Long> {
    // Add any custom methods if needed
}
