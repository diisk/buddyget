package br.dev.diisk.infra.transaction.expense.jpas;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.transaction.expense.ExpenseRecurring;

public interface ExpenseRecurringJPA extends JpaRepository<ExpenseRecurring, Long> {

}
