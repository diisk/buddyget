package br.dev.diisk.application.finance.income_transaction.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.income_recurring.cases.AdjustIncomeRecurringCase;
import br.dev.diisk.application.finance.income_transaction.dtos.UpdateIncomeTransactionParams;
import br.dev.diisk.application.monthly_summary.cases.UpdateMonthlySummaryCase;
import br.dev.diisk.application.monthly_summary.dtos.UpdateMonthlySummaryParams;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateIncomeTransactionCase {

    private final IIncomeTransactionRepository incomeRepository;
    private final GetIncomeTransactionCase getIncomeTransactionCase;
    private final UpdateMonthlySummaryCase updateMonthlySummaryCase;
    private final AdjustIncomeRecurringCase adjustIncomeRecurringCase;

    @Transactional
    public IncomeTransaction execute(User user, Long incomeTransactionId, UpdateIncomeTransactionParams params) {
        IncomeTransaction incomeTransaction = getIncomeTransactionCase.execute(user, incomeTransactionId);

        BigDecimal previousValue = incomeTransaction.getValue();
        LocalDateTime previousPaymentDate = incomeTransaction.getPaymentDate();
        incomeTransaction.update(params.getDescription(), params.getValue(), params.getPaymentDate());
        incomeRepository.save(incomeTransaction);

        if (incomeTransaction.getIncomeRecurring() != null)
            adjustIncomeRecurringCase.execute(incomeTransaction.getIncomeRecurring());

        BigDecimal value = incomeTransaction.getValue();
        LocalDateTime paymentDate = incomeTransaction.getPaymentDate();

        UpdateMonthlySummaryParams updateMonthlySummaryParams = new UpdateMonthlySummaryParams();
        updateMonthlySummaryParams.setPreviousValue(previousValue);
        updateMonthlySummaryParams.setPreviousDate(previousPaymentDate);
        updateMonthlySummaryParams.setNewValue(value);
        updateMonthlySummaryParams.setNewDate(paymentDate);
        updateMonthlySummaryParams.setCategory(incomeTransaction.getCategory());
        updateMonthlySummaryCase.execute(user, updateMonthlySummaryParams);

        return incomeTransaction;
    }
}
