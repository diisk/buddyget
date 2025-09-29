package br.dev.diisk.application.finance.expense_recurring.cases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.application.finance.expense_recurring.dtos.EndExpenseRecurringParams;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EndExpenseRecurringCase {

    private final GetExpenseRecurringCase getExpenseRecurringCase;

    private final IExpenseRecurringRepository expenseRecurringRepository;
    private final IExpenseTransactionRepository expenseTransactionRepository;

    @Transactional
    public ExpenseRecurring execute(User user, EndExpenseRecurringParams params, Long expenseRecurringId) {
        ExpenseRecurring expenseRecurring = getExpenseRecurringCase.execute(user, expenseRecurringId);
        List<ExpenseTransaction> paidTransactions = expenseTransactionRepository
                .findAllRecurringRelatedBy(List.of(expenseRecurringId));

        for (ExpenseTransaction transaction : paidTransactions) {
            if (transaction.getRecurringReferenceDate() != null
                    && transaction.getRecurringReferenceDate().isAfter(params.getEndDate()))
                throw new BusinessException(getClass(),
                        "Não é possível definir uma data de término anterior a uma despesa já paga.");

        }

        expenseRecurring.defineEndDate(LocalDateTime.now());
        expenseRecurringRepository.save(expenseRecurring);

        return expenseRecurring;

    }
}
