package br.dev.diisk.application.finance.expense_recurring.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetExpenseRecurringCase {

    private final IExpenseRecurringRepository expenseRecurringRepository;

    @Transactional
    public ExpenseRecurring execute(User user, Long expenseRecurringId) {
        ExpenseRecurring expenseRecurring = expenseRecurringRepository.findById(expenseRecurringId).orElse(null);
        if (expenseRecurring == null || !expenseRecurring.getUserId().equals(user.getId())) {
            throw new DatabaseValueNotFoundException(getClass(), expenseRecurringId.toString());
        }
        return expenseRecurring;
    }
}
