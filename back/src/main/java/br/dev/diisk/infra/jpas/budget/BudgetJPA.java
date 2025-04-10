package br.dev.diisk.infra.jpas.budget;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.budget.Budget;

public interface BudgetJPA extends JpaRepository<Budget, Long> {
    // Add any custom methods if needed
}
