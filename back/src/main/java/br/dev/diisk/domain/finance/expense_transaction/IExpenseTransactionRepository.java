package br.dev.diisk.domain.finance.expense_transaction;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IExpenseTransactionRepository extends IBaseRepository<ExpenseTransaction> {

    List<ExpenseTransaction> findAllPendingBy(Long userId);

    Page<ExpenseTransaction> findAllPaidBy(Long userId, ListExpenseTransactionsFilter filter, Pageable pageable);

    List<ExpenseTransaction> findAllRecurringRelatedBy(List<Long> expenseRecurringIds);

}
