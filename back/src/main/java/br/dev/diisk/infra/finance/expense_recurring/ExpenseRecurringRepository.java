package br.dev.diisk.infra.finance.expense_recurring;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_recurring.ListExpenseRecurringsFilter;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class ExpenseRecurringRepository extends BaseRepository<ExpenseRecurringJPA, ExpenseRecurring>
        implements IExpenseRecurringRepository {

    public ExpenseRecurringRepository(ExpenseRecurringJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<ExpenseRecurring> findAllBy(Long userId, ListExpenseRecurringsFilter filter, Pageable pageable) {
        return jpa.findAllBy(userId, filter.getSearchString(), pageable);
    }

    @Override
    public List<ExpenseRecurring> findAllActiveBy(Long userId) {
        return jpa.findAllByUser_IdAndActiveTrueAndDeletedFalse(userId);
    }

}
