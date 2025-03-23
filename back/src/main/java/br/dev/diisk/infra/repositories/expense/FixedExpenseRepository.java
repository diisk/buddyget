package br.dev.diisk.infra.repositories.expense;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import br.dev.diisk.infra.jpas.expense.FixedExpenseJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class FixedExpenseRepository extends BaseRepository<FixedExpenseJPA, FixedExpense> implements IFixedExpenseRepository {

    public FixedExpenseRepository(FixedExpenseJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
