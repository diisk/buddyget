package br.dev.diisk.application.monthly_summary.cases;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.budget.cases.GetBudgetCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.monthly_summary.dtos.GetMonthlySummaryParams;
import br.dev.diisk.domain.budget.Budget;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddMonthlySummaryValueCase {

    private final IMonthlySummaryRepository monthlySummaryRepository;
    private final GetBudgetCase getBudgetCase;
    private final GetMonthlySummaryCase getMonthlySummaryCase;

    @Transactional
    public MonthlySummary execute(User user, AddMonthlySummaryValueParams params) {
        Integer month = params.getMonth();
        Integer year = params.getYear();
        Category category = params.getCategory();
        BigDecimal value = params.getValue();

        if (category == null)
            throw new NullOrEmptyException(getClass(), "category");

        MonthlySummary monthlySummary = getMonthlySummaryCase.execute(user,
                new GetMonthlySummaryParams(month, year, category.getId()));

        if (monthlySummary == null) {
            Long budgetLimit = 0L;
            Budget budget = getBudgetCase.execute(user, category);
            if (budget != null)
                budgetLimit = budget.getLimitValue();

            monthlySummary = new MonthlySummary(user, month, year, BigDecimal.ZERO, budgetLimit, category);
        }

        monthlySummary.addAmount(value);

        monthlySummaryRepository.save(monthlySummary);
        return monthlySummary;

    }
}
