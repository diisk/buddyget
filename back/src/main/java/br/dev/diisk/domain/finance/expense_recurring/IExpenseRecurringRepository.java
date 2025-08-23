package br.dev.diisk.domain.finance.expense_recurring;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IExpenseRecurringRepository extends IBaseRepository<ExpenseRecurring> {

    Page<ExpenseRecurring> findAllBy(Long userId, ListExpenseRecurringsFilter filter, Pageable pageable);

    List<ExpenseRecurring> findAllActiveBy(Long userId);

}
