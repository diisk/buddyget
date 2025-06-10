package br.dev.diisk.infra.transaction.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;
import br.dev.diisk.domain.transaction.expense.IExpenseRepository;
import br.dev.diisk.domain.transaction.expense.ListExpenseFilter;
import br.dev.diisk.infra.shared.BaseRepository;
import br.dev.diisk.infra.transaction.expense.jpas.ExpenseTransactionJPA;

@Repository
public class ExpenseTransactionRepository extends BaseRepository<ExpenseTransactionJPA, ExpenseTransaction> implements IExpenseRepository {

    public ExpenseTransactionRepository(ExpenseTransactionJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<ExpenseTransaction> findBy(Long userId, ListExpenseFilter filter, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }
}
