package br.dev.diisk.infra.repositories.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.transaction.ExpenseTransaction;
import br.dev.diisk.domain.filters.expense.ListExpenseFilter;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import br.dev.diisk.infra.jpas.transaction.ExpenseTransactionJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

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
