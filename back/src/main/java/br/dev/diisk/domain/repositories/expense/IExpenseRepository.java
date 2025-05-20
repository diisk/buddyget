package br.dev.diisk.domain.repositories.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.entities.transaction.ExpenseTransaction;
import br.dev.diisk.domain.filters.expense.ListExpenseFilter;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface IExpenseRepository extends IBaseRepository<ExpenseTransaction> {
    
    Page<ExpenseTransaction> findBy(Long userId, ListExpenseFilter filter, Pageable pageable);

}
