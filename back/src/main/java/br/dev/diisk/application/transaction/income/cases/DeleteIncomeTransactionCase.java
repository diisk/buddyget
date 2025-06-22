package br.dev.diisk.application.transaction.income.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.cases.RemoveMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.RemoveMonthlySummaryValueParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.transaction.income.IIncomeTransactionRepository;
import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteIncomeTransactionCase {

    private final IIncomeTransactionRepository incomeRepository;
    private final RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;

    @Transactional
    public void execute(User user, Long incomeTransactionId) {
        IncomeTransaction incomeTransaction = incomeRepository
                .findById(incomeTransactionId).orElse(null);

        if (incomeTransaction == null || incomeTransaction.getUserId() != user.getId())
            return;

        incomeTransaction.delete();
        incomeRepository.save(incomeTransaction);

        LocalDateTime receiptDate = incomeTransaction.getReceiptDate();
        if (receiptDate != null) {
            Category category = incomeTransaction.getCategory();
            BigDecimal value = incomeTransaction.getValue();
            removeMonthlySummaryValueCase.execute(user,
                    new RemoveMonthlySummaryValueParams(receiptDate.getMonthValue(), receiptDate.getYear(),
                            value, category));
        }

    }
}
