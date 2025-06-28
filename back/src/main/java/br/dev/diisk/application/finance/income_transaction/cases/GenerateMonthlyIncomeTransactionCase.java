package br.dev.diisk.application.finance.income_transaction.cases;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.income_recurring.cases.ListCurrentIncomeRecurringsCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenerateMonthlyIncomeTransactionCase {

    private final ListCurrentIncomeRecurringsCase listCurrentIncomeRecurringsCase;
    private final IIncomeTransactionRepository incomeTransactionRepository;
    private final AddIncomeTransactionCase addIncomeTransactionCase;
    private final UtilService utilService;

    @Transactional
    public void execute() {
        Set<IncomeRecurring> recurrings = listCurrentIncomeRecurringsCase.execute();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = utilService.getFirstDayMonthReference(now);
        LocalDateTime firstDayOfNextMonth = firstDayOfMonth.plusMonths(1);
        for (IncomeRecurring recurring : recurrings) {
            boolean hasTransactionThisMonth = incomeTransactionRepository.existsByRecurring(recurring.getId(),
                    firstDayOfMonth, firstDayOfNextMonth);
            if (hasTransactionThisMonth)
                continue;
            AddIncomeTransactionParams params = new AddIncomeTransactionParams();
            params.setCategoryId(recurring.getCategoryId());
            params.setDescription(recurring.getDescription());
            params.setValue(recurring.getValue());
            params.setIncomeRecurringId(recurring.getId());
            params.setRecurringReferenceDate(firstDayOfMonth);
            addIncomeTransactionCase.execute(recurring.getUser(), params);
        }
    }
}
