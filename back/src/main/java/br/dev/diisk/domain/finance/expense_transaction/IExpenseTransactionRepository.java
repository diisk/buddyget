package br.dev.diisk.domain.finance.expense_transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IExpenseTransactionRepository extends IBaseRepository<ExpenseTransaction> {
    
    Page<ExpenseTransaction> findAllBy(Long userId, ListExpenseTransactionsFilter filter, Pageable pageable);

}
