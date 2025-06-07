package br.dev.diisk.infra.jpas.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.transaction.ExpenseRecurring;

public interface ExpenseRecurringJPA extends JpaRepository<ExpenseRecurring, Long> {

}
