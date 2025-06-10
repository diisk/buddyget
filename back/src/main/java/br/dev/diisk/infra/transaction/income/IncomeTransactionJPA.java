package br.dev.diisk.infra.transaction.income;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.transaction.income.IncomeTransaction;

public interface IncomeTransactionJPA extends JpaRepository<IncomeTransaction, Long> {

    

}
