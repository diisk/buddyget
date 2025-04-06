package br.dev.diisk.infra.repositories.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.filters.expense.ListFixedExpenseFilter;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import br.dev.diisk.infra.jpas.expense.FixedExpenseJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class FixedExpenseRepository extends BaseRepository<FixedExpenseJPA, FixedExpense>
        implements IFixedExpenseRepository {

    public FixedExpenseRepository(FixedExpenseJPA jpa) {
        super(jpa);
    }

    @Override
    public Page<FixedExpense> findBy(Long userId, ListFixedExpenseFilter filter, Pageable pageable) {
        return jpa.findAllWithFilter(
                userId,
                filter.getCategoryId(),
                filter.getActive(), pageable);
    }

}
