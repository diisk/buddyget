package br.dev.diisk.domain.finance.income_transaction;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IIncomeTransactionRepository extends IBaseRepository<IncomeTransaction> {

    Page<IncomeTransaction> findAllPaidBy(Long userId, ListPaidIncomeTransactionsFilter filter, Pageable pageable);

    List<IncomeTransaction> findAllPendingBy(Long userId);

    List<IncomeTransaction> findAllRecurringRelatedBy(List<Long> incomeRecurringIds);

}
