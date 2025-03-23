package br.dev.diisk.infra.jpas.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import br.dev.diisk.domain.entities.expense.Expense;

public interface ExpenseJPA extends JpaRepository<Expense, Long> {
    // Add any custom methods if needed
}
