package br.dev.diisk.application.finance.income_recurring.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.finance.income_recurring.dtos.AddIncomeRecurringParams;
import br.dev.diisk.application.finance.income_transaction.cases.AddIncomeTransactionCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddIncomeRecurringCase {

    private final IIncomeRecurringRepository incomeRecurringRepository;
    private final GetCategoryCase getCategoryCase;
    private final AddIncomeTransactionCase addIncomeTransactionCase;
    private final UtilService utilService;

    @Transactional
    public IncomeRecurring execute(User user, AddIncomeRecurringParams params) {
        String description = params.getDescription();
        Long categoryId = params.getCategoryId();
        BigDecimal value = params.getValue();
        LocalDateTime startDate = params.getStartDate();
        LocalDateTime endDate = params.getEndDate() != null ? utilService.getLastDayMonthReference(params.getEndDate())
                : null;
        Category category = null;
        if (categoryId != null) {
            category = getCategoryCase.execute(user, categoryId);
        }

        IncomeRecurring incomeRecurring = new IncomeRecurring(description,
                new DataRange(startDate, endDate), category,
                value, user);

        incomeRecurringRepository.save(incomeRecurring);
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime referenceDate = utilService.getFirstDayMonthReference(startDate);
        LocalDateTime lastAvailableDate = endDate != null && endDate.isBefore(today) ? endDate : today;
        if (referenceDate.isBefore(startDate))
            referenceDate = referenceDate.plusMonths(1);

        while (!referenceDate.isAfter(lastAvailableDate)) {

            AddIncomeTransactionParams transactionParams = new AddIncomeTransactionParams();
            transactionParams.setDescription(description);
            transactionParams.setCategoryId(categoryId);
            transactionParams.setIncomeRecurringId(incomeRecurring.getId());
            transactionParams.setValue(value);
            transactionParams.setRecurringReferenceDate(referenceDate);
            addIncomeTransactionCase.execute(user, transactionParams);

            referenceDate = referenceDate.plusMonths(1);
        }

        return incomeRecurring;

    }
}
