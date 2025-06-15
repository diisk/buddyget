package br.dev.diisk.domain.transaction.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;
import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;

public interface IIncomeTransactionRepository extends IBaseRepository<IncomeTransaction> {

    Page<IncomeTransaction> findAllBy(Long userId, ListIncomeTransactionsFilter filter, Pageable pageable);

}
