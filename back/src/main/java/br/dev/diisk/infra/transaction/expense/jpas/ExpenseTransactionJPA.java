package br.dev.diisk.infra.transaction.expense.jpas;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;

public interface ExpenseTransactionJPA extends JpaRepository<ExpenseTransaction, Long> {

    

}
