package br.dev.diisk.domain.repositories.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.entities.transaction.ExpenseRecurring;
import br.dev.diisk.domain.filters.expense.ListFixedExpenseFilter;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface IFixedExpenseRepository extends IBaseRepository<ExpenseRecurring> {
    
    Page<ExpenseRecurring> findBy(Long userId, ListFixedExpenseFilter filter, Pageable pageable);

}
