package br.dev.diisk.application.transaction.income.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.cases.AddMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.cases.RemoveMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.domain.transaction.income.IIncomeTransactionRepository;
import br.dev.diisk.domain.transaction.income.dtos.UpdateIncomeTransactionParams;
import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateIncomeTransactionCase {

    private final IIncomeTransactionRepository incomeRepository;
    private final GetIncomeTransactionCase getIncomeTransactionCase;
    private final AddMonthlySummaryValueCase addMonthlySummaryValueCase;
    private final RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;

    @Transactional
    public IncomeTransaction execute(User user, Long incomeTransactionId, UpdateIncomeTransactionParams params) {
        IncomeTransaction incomeTransaction = getIncomeTransactionCase.execute(user, incomeTransactionId);

        BigDecimal previousValue = incomeTransaction.getValue();
        LocalDateTime previousReceiptDate = incomeTransaction.getDate();
        Boolean hadReceiptDate = incomeTransaction.getDate() != null;

        incomeTransaction.update(params.getDescription(), params.getValue(), params.getReceiptDate());
        incomeRepository.save(incomeTransaction);

        BigDecimal value = incomeTransaction.getValue();
        LocalDateTime receiptDate = incomeTransaction.getDate();

        Boolean isValueChanged = !previousValue.equals(value);

        if (hadReceiptDate && (receiptDate == null || isValueChanged))
            removeMonthlySummaryValueCase.execute(user,
                    new AddMonthlySummaryValueParams(previousReceiptDate.getMonthValue(), previousReceiptDate.getYear(),
                            previousValue, incomeTransaction.getCategory()));

        if (receiptDate != null && (!hadReceiptDate || isValueChanged))
            addMonthlySummaryValueCase.execute(user,
                    new AddMonthlySummaryValueParams(receiptDate.getMonthValue(), receiptDate.getYear(),
                            value, incomeTransaction.getCategory()));

        return incomeTransaction;
    }
}
