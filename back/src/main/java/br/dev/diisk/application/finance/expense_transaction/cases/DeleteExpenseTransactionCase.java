package br.dev.diisk.application.finance.expense_transaction.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.cases.RemoveMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.RemoveMonthlySummaryValueParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteExpenseTransactionCase {

    private final IExpenseTransactionRepository expenseRepository;
    private final RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;

    @Transactional
    public void execute(User user, Long expenseTransactionId, Boolean force) {
        ExpenseTransaction expenseTransaction = expenseRepository
                .findById(expenseTransactionId).orElse(null);

        if (expenseTransaction == null
                || expenseTransaction.getUserId() != user.getId()
                || (expenseTransaction.getExpenseRecurring() != null && !force))
            return;

        expenseTransaction.delete();
        expenseRepository.save(expenseTransaction);

        LocalDateTime paymentDate = expenseTransaction.getPaymentDate();
        if (paymentDate != null) {
            Category category = expenseTransaction.getCategory();
            BigDecimal value = expenseTransaction.getValue();
            removeMonthlySummaryValueCase.execute(user,
                    new RemoveMonthlySummaryValueParams(paymentDate.getMonthValue(), paymentDate.getYear(),
                            value, category));
        }

    }
}
