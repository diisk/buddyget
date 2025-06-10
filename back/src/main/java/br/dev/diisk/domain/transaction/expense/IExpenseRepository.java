package br.dev.diisk.domain.transaction.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IExpenseRepository extends IBaseRepository<ExpenseTransaction> {
    
    Page<ExpenseTransaction> findBy(Long userId, ListExpenseFilter filter, Pageable pageable);

}
