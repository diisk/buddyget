package br.dev.diisk.application.transaction.expense.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.cases.RemoveMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.RemoveMonthlySummaryValueParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;
import br.dev.diisk.domain.transaction.expense.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteExpenseTransactionCase {

    private final IExpenseTransactionRepository expenseRepository;
    private final RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;

    @Transactional
    public void execute(User user, Long expenseTransactionId) {
        ExpenseTransaction expenseTransaction = expenseRepository
                .findById(expenseTransactionId).orElse(null);

        if (expenseTransaction == null || expenseTransaction.getUserId() != user.getId())
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
