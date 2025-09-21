package br.dev.diisk.infra.finance.expense_transaction;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.finance.expense_transaction.ListPaidExpenseTransactionsFilter;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class ExpenseTransactionRepository extends BaseRepository<ExpenseTransactionJPA, ExpenseTransaction>
        implements IExpenseTransactionRepository {

    public ExpenseTransactionRepository(ExpenseTransactionJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<ExpenseTransaction> findAllPaidBy(Long userId, ListPaidExpenseTransactionsFilter filter,
            Pageable pageable) {
        return jpa.findAllPaidBy(userId, filter.getStartDate(), filter.getEndDate(), filter.getSearchString(),
                pageable);
    }

    @Override
    public List<ExpenseTransaction> findAllPendingBy(Long userId) {
        return jpa.findAllByUser_IdAndDateIsNullAndDeletedFalse(userId);
    }

    @Override
    public List<ExpenseTransaction> findAllRecurringRelatedBy(List<Long> expenseRecurringIds) {
        return jpa.findAllByExpenseRecurring_IdInAndDeletedFalse(expenseRecurringIds);
    }
}
