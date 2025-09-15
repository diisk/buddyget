package br.dev.diisk.application.finance.expense_recurring.cases;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.application.finance.expense_transaction.cases.DeleteExpenseTransactionCase;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteExpenseRecurringCase {

    private final IExpenseRecurringRepository expenseRecurringRepository;
    private final IExpenseTransactionRepository expenseTransactionRepository;
    private final DeleteExpenseTransactionCase deleteExpenseTransactionCase;

    @Transactional
    public void execute(User user, Long expenseRecurringId) {
        ExpenseRecurring expenseRecurring = expenseRecurringRepository.findById(expenseRecurringId).orElse(null);
        if (expenseRecurring == null || expenseRecurring.getUserId() != user.getId())
            return;

        expenseRecurring.delete();

        List<ExpenseTransaction> relatedTransactions = expenseTransactionRepository
                .findAllRecurringRelatedBy(List.of(expenseRecurringId));

        for (ExpenseTransaction transaction : relatedTransactions)
            deleteExpenseTransactionCase.execute(user, transaction.getId(), true);

        expenseRecurringRepository.save(expenseRecurring);

    }
}
