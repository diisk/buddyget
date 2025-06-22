package br.dev.diisk.infra.transaction.expense;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.transaction.expense.ExpenseRecurring;
import br.dev.diisk.domain.transaction.expense.IExpenseRecurringRepository;
import br.dev.diisk.infra.shared.BaseRepository;
import br.dev.diisk.infra.transaction.expense.jpas.ExpenseRecurringJPA;

@Repository
public class ExpenseRecurringRepository extends BaseRepository<ExpenseRecurringJPA, ExpenseRecurring>
        implements IExpenseRecurringRepository {

    public ExpenseRecurringRepository(ExpenseRecurringJPA jpa) {
        super(jpa);
    }

}
