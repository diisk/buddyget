package br.dev.diisk.infra.jpas.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.transaction.IncomeRecurring;

public interface IncomeRecurringJPA extends JpaRepository<IncomeRecurring, Long> {

    

}
