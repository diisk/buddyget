package br.dev.diisk.infra.finance.expense_transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.finance.expense_transaction.ListExpenseTransactionsFilter;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class ExpenseTransactionRepository extends BaseRepository<ExpenseTransactionJPA, ExpenseTransaction>
        implements IExpenseTransactionRepository {

    public ExpenseTransactionRepository(ExpenseTransactionJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<ExpenseTransaction> findAllBy(Long userId, ListExpenseTransactionsFilter filter, Pageable pageable) {
        return jpa.findAllBy(userId, filter.getStartDate(), filter.getEndDate(), filter.getSearchString(), pageable);
    }
}
