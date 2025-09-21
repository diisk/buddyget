package br.dev.diisk.application.finance.income_transaction.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.finance.income_recurring.cases.AdjustIncomeRecurringCase;
import br.dev.diisk.application.finance.income_recurring.cases.GetIncomeRecurringCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.monthly_summary.cases.AddMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.validations.DateOutOfRangeValidation;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddIncomeTransactionCase {

    private final IIncomeTransactionRepository incomeRepository;
    private final GetCategoryCase getCategoryCase;
    private final GetIncomeRecurringCase getIncomeRecurringCase;
    private final GetGoalCase getGoalCase;
    private final AddMonthlySummaryValueCase addMonthlySummaryValueCase;
    private final UtilService utilService;
    private final AdjustIncomeRecurringCase adjustIncomeRecurringCase;

    @Transactional
    public IncomeTransaction execute(User user, AddIncomeTransactionParams params) {
        String description = params.getDescription();
        Long categoryId = params.getCategoryId();
        Long incomeRecurringId = params.getIncomeRecurringId();
        LocalDateTime recurringReferenceDate = params.getRecurringReferenceDate();
        BigDecimal value = params.getValue();
        LocalDateTime date = params.getPaymentDate();
        Long goalId = params.getGoalId();
        LocalDateTime paymentDate = params.getPaymentDate();

        Category category = null;
        if (categoryId != null)
            category = getCategoryCase.execute(user, categoryId);

        Goal goal = null;
        if (goalId != null)
            goal = getGoalCase.execute(user, goalId);

        IncomeRecurring incomeRecurring = null;
        if (incomeRecurringId != null) {
            incomeRecurring = getIncomeRecurringCase.execute(user, incomeRecurringId);
            if (recurringReferenceDate == null)
                throw new BusinessException(getClass(),
                        "A data da referência da recorrencia deve ser informada se a receita for recorrente.");

            if (paymentDate == null)
                throw new BusinessException(getClass(),
                        "A data do pagamento deve ser informada se a receita for recorrente.");

            Period recurringPeriod = utilService.getPeriodReference(incomeRecurring.getPeriod());

            new DateOutOfRangeValidation(recurringReferenceDate, recurringPeriod).validate(getClass());

            List<IncomeTransaction> relatedTransactions = incomeRepository
                    .findAllRecurringRelatedBy(List.of(incomeRecurring.getId()));
            Boolean hasTransactionInReferenceMonth = relatedTransactions.stream().anyMatch(rt -> {
                return rt.getRecurringReferenceDate().getMonthValue() == recurringReferenceDate.getMonthValue() &&
                        rt.getRecurringReferenceDate().getYear() == recurringReferenceDate.getYear();
            });

            if (hasTransactionInReferenceMonth)
                throw new BusinessException(getClass(),
                        "Já existe uma receita vinculada a essa recorrência na referência informada.");

        }

        IncomeTransaction incomeTransaction = new IncomeTransaction(description, category, value, date, user);

        if (goal != null)
            incomeTransaction.addGoal(goal);

        if (incomeRecurring != null)
            incomeTransaction.addIncomeRecurring(incomeRecurring, recurringReferenceDate);

        incomeRepository.save(incomeTransaction);

        if (incomeRecurring != null)
            adjustIncomeRecurringCase.execute(incomeRecurring);

        if (date != null)
            addMonthlySummaryValueCase.execute(user,
                    new AddMonthlySummaryValueParams(date.getMonthValue(), date.getYear(), value, category));

        return incomeTransaction;
    }
}
