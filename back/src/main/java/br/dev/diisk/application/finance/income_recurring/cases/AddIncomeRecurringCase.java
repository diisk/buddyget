package br.dev.diisk.application.finance.income_recurring.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.finance.income_recurring.dtos.AddIncomeRecurringParams;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddIncomeRecurringCase {

    private final IIncomeRecurringRepository incomeRecurringRepository;
    private final GetCategoryCase getCategoryCase;
    private final UtilService utilService;
    private final GetGoalCase getGoalCase;

    @Transactional
    public IncomeRecurring execute(User user, AddIncomeRecurringParams params) {
        String description = params.getDescription();
        Long categoryId = params.getCategoryId();
        Long goalId = params.getGoalId();
        BigDecimal value = params.getValue();

        LocalDateTime startDate = params.getStartDate();
        LocalDateTime endDate = params.getEndDate() != null ? utilService.getLastDayMonthReference(params.getEndDate())
                : null;

        Category category = null;
        if (categoryId != null)
            category = getCategoryCase.execute(user, categoryId);

        Goal goal = null;
        if (goalId != null)
            goal = getGoalCase.execute(user, goalId);

        IncomeRecurring incomeRecurring = new IncomeRecurring(description,
                new Period(startDate, endDate), category,
                value, user);

        if (goal != null)
            incomeRecurring.addGoal(goal);

        incomeRecurringRepository.save(incomeRecurring);

        return incomeRecurring;

    }
}
