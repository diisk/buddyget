package br.dev.diisk.infra.repositories.expense;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import br.dev.diisk.infra.jpas.expense.ExpenseJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class ExpenseRepository extends BaseRepository<ExpenseJPA, Expense> implements IExpenseRepository {

    public ExpenseRepository(ExpenseJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
