package br.dev.diisk.infra.finance.expense_recurring;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class ExpenseRecurringRepository extends BaseRepository<ExpenseRecurringJPA, ExpenseRecurring>
        implements IExpenseRecurringRepository {

    public ExpenseRecurringRepository(ExpenseRecurringJPA jpa) {
        super(jpa);
    }

}
