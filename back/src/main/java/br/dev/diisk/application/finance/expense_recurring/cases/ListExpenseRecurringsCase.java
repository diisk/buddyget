package br.dev.diisk.application.finance.expense_recurring.cases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ListExpenseRecurringsFilter;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListExpenseRecurringsCase {

    private final IExpenseRecurringRepository expenseRecurringRepository;

    @Transactional
    public Page<ExpenseRecurring> execute(User user, ListExpenseRecurringsFilter filter, Pageable pageable) {

        return expenseRecurringRepository.findAllBy(user.getId(), filter, pageable);
    }
}
