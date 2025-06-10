package br.dev.diisk.domain.transaction.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IFixedExpenseRepository extends IBaseRepository<ExpenseRecurring> {
    
    Page<ExpenseRecurring> findBy(Long userId, ListFixedExpenseFilter filter, Pageable pageable);

}
