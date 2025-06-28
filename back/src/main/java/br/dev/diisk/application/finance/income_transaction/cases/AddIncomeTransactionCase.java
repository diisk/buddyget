package br.dev.diisk.application.finance.income_transaction.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.finance.income_recurring.cases.GetIncomeRecurringCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.monthly_summary.cases.AddMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
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

    @Transactional
    public IncomeTransaction execute(User user, AddIncomeTransactionParams params) {
        String description = params.getDescription();
        Long categoryId = params.getCategoryId();
        Long incomeRecurringId = params.getIncomeRecurringId();
        LocalDateTime recurringReferenceDate = params.getRecurringReferenceDate();
        BigDecimal value = params.getValue();
        LocalDateTime date = params.getReceiptDate();
        Long goalId = params.getGoalId();

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

        }

        IncomeTransaction incomeTransaction = new IncomeTransaction(description, category, value, date, user);

        if (goal != null)
            incomeTransaction.addGoal(goal);

        if (incomeRecurring != null && recurringReferenceDate != null)
            incomeTransaction.addIncomeRecurring(incomeRecurring, recurringReferenceDate);

        incomeRepository.save(incomeTransaction);

        if (date != null)
            addMonthlySummaryValueCase.execute(user,
                    new AddMonthlySummaryValueParams(date.getMonthValue(), date.getYear(), value, category));

        return incomeTransaction;
    }
}
