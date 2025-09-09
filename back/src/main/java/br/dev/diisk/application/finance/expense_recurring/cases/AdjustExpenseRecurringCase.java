package br.dev.diisk.application.finance.expense_recurring.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.AdjustRecurringCase;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.infra.finance.expense_recurring.ExpenseRecurringRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdjustExpenseRecurringCase {

    private final AdjustRecurringCase adjustRecurringCase;
    private final ExpenseRecurringRepository expenseRecurringRepository;

    @Transactional
    public void execute(ExpenseRecurring expenseRecurring) {
        adjustRecurringCase.execute(expenseRecurring, er -> {
            expenseRecurringRepository.save((ExpenseRecurring) er);
            return null;
        });
    }
}
