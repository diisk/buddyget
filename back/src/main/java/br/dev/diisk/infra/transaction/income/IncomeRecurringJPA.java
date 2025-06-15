package br.dev.diisk.infra.transaction.income;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.transaction.income.entities.IncomeRecurring;

public interface IncomeRecurringJPA extends JpaRepository<IncomeRecurring, Long> {

    

}
