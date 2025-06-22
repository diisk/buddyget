package br.dev.diisk.application.transaction.expense.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.cases.UpdateMonthlySummaryCase;
import br.dev.diisk.application.monthly_summary.dtos.UpdateMonthlySummaryParams;
import br.dev.diisk.application.transaction.expense.dtos.UpdateExpenseTransactionParams;
import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;
import br.dev.diisk.domain.transaction.expense.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateExpenseTransactionCase {

    private final IExpenseTransactionRepository expenseRepository;
    private final GetExpenseTransactionCase getExpenseTransactionCase;
    private final UpdateMonthlySummaryCase updateMonthlySummaryCase;

    @Transactional
    public ExpenseTransaction execute(User user, Long expenseTransactionId, UpdateExpenseTransactionParams params) {
        ExpenseTransaction expenseTransaction = getExpenseTransactionCase.execute(user, expenseTransactionId);

        BigDecimal previousValue = expenseTransaction.getValue();
        LocalDateTime previousPaymentDate = expenseTransaction.getPaymentDate();

        expenseTransaction.update(params.getDescription(), params.getValue(), params.getPaymentDate(),
                params.getDueDate());
        expenseRepository.save(expenseTransaction);

        BigDecimal value = expenseTransaction.getValue();
        LocalDateTime paymentDate = expenseTransaction.getPaymentDate();

        UpdateMonthlySummaryParams updateMonthlySummaryParams = new UpdateMonthlySummaryParams();
        updateMonthlySummaryParams.setPreviousValue(previousValue);
        updateMonthlySummaryParams.setPreviousDate(previousPaymentDate);
        updateMonthlySummaryParams.setNewValue(value);
        updateMonthlySummaryParams.setNewDate(paymentDate);
        updateMonthlySummaryParams.setCategory(expenseTransaction.getCategory());
        updateMonthlySummaryCase.execute(user, updateMonthlySummaryParams);

        return expenseTransaction;
    }
}
