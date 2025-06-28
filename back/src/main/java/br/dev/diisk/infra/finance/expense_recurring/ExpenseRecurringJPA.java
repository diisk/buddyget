package br.dev.diisk.infra.finance.expense_recurring;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;

public interface ExpenseRecurringJPA extends JpaRepository<ExpenseRecurring, Long> {

}
