package br.dev.diisk.infra.repositories.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.filters.expense.ListExpenseFilter;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import br.dev.diisk.infra.jpas.expense.ExpenseJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class ExpenseRepository extends BaseRepository<ExpenseJPA, Expense> implements IExpenseRepository {

    public ExpenseRepository(ExpenseJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<Expense> findBy(Long userId, ListExpenseFilter filter, Pageable pageable) {
        return jpa.findAllWithFilter(
                userId,
                filter.getStartDueDate(),
                filter.getEndDueDate(),
                filter.getStartPaymentDate(),
                filter.getEndPaymentDate(),
                filter.getCategoryId(),
                filter.getCreditCardId(),
                filter.getFixedExpenseId(),
                pageable);
    }
}
