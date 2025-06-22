package br.dev.diisk.application.transaction.income.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.cases.UpdateMonthlySummaryCase;
import br.dev.diisk.application.monthly_summary.dtos.UpdateMonthlySummaryParams;
import br.dev.diisk.application.transaction.income.dtos.UpdateIncomeTransactionParams;
import br.dev.diisk.domain.transaction.income.IIncomeTransactionRepository;
import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateIncomeTransactionCase {

    private final IIncomeTransactionRepository incomeRepository;
    private final GetIncomeTransactionCase getIncomeTransactionCase;
    private final UpdateMonthlySummaryCase updateMonthlySummaryCase;

    @Transactional
    public IncomeTransaction execute(User user, Long incomeTransactionId, UpdateIncomeTransactionParams params) {
        IncomeTransaction incomeTransaction = getIncomeTransactionCase.execute(user, incomeTransactionId);

        BigDecimal previousValue = incomeTransaction.getValue();
        LocalDateTime previousReceiptDate = incomeTransaction.getReceiptDate();

        incomeTransaction.update(params.getDescription(), params.getValue(), params.getReceiptDate());
        incomeRepository.save(incomeTransaction);

        BigDecimal value = incomeTransaction.getValue();
        LocalDateTime receiptDate = incomeTransaction.getReceiptDate();

        UpdateMonthlySummaryParams updateMonthlySummaryParams = new UpdateMonthlySummaryParams();
        updateMonthlySummaryParams.setPreviousValue(previousValue);
        updateMonthlySummaryParams.setPreviousDate(previousReceiptDate);
        updateMonthlySummaryParams.setNewValue(value);
        updateMonthlySummaryParams.setNewDate(receiptDate);
        updateMonthlySummaryParams.setCategory(incomeTransaction.getCategory());
        updateMonthlySummaryCase.execute(user, updateMonthlySummaryParams);

        return incomeTransaction;
    }
}
