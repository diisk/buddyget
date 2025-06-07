package br.dev.diisk.infra.jpas.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import br.dev.diisk.domain.entities.transaction.IncomeTransaction;

public interface IncomeTransactionJPA extends JpaRepository<IncomeTransaction, Long> {

    

}
