package br.dev.diisk.domain.repositories.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.filters.expense.ListFixedExpenseFilter;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface IFixedExpenseRepository extends IBaseRepository<FixedExpense> {
    
    Page<FixedExpense> findBy(Long userId, ListFixedExpenseFilter filter, Pageable pageable);

}
