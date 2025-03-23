package br.dev.diisk.infra.jpas.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import br.dev.diisk.domain.entities.expense.FixedExpense;

public interface FixedExpenseJPA extends JpaRepository<FixedExpense, Long> {
    // Add any custom methods if needed
}
