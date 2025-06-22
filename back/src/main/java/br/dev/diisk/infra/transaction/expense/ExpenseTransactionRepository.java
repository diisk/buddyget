package br.dev.diisk.infra.transaction.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;
import br.dev.diisk.domain.transaction.expense.IExpenseTransactionRepository;
import br.dev.diisk.domain.transaction.expense.ListExpenseTransactionsFilter;
import br.dev.diisk.infra.shared.BaseRepository;
import br.dev.diisk.infra.transaction.expense.jpas.ExpenseTransactionJPA;

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
