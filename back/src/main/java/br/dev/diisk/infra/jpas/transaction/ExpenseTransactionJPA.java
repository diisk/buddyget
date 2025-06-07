package br.dev.diisk.infra.jpas.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.transaction.ExpenseTransaction;

public interface ExpenseTransactionJPA extends JpaRepository<ExpenseTransaction, Long> {

    

}
