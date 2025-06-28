package br.dev.diisk.application.monthly_summary.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.monthly_summary.dtos.RemoveMonthlySummaryValueParams;
import br.dev.diisk.application.monthly_summary.dtos.UpdateMonthlySummaryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateMonthlySummaryCase {

    private final RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;
    private final AddMonthlySummaryValueCase addMonthlySummaryValueCase;

    @Transactional
    public Optional<MonthlySummary> execute(User user, UpdateMonthlySummaryParams params) {

        BigDecimal previousValue = params.getPreviousValue();
        LocalDateTime previousDate = params.getPreviousDate();
        BigDecimal newValue = params.getNewValue();
        LocalDateTime newDate = params.getNewDate();
        Category category = params.getCategory();

        Boolean isValueChanged = !previousValue.equals(newValue);

        MonthlySummary monthlySummary = null;

        if (previousDate != null && (newDate == null || isValueChanged))
            monthlySummary = removeMonthlySummaryValueCase.execute(user,
                    new RemoveMonthlySummaryValueParams(previousDate.getMonthValue(), previousDate.getYear(),
                            previousValue, category));

        if (newDate != null && (previousDate == null || isValueChanged))
            monthlySummary = addMonthlySummaryValueCase.execute(user,
                    new AddMonthlySummaryValueParams(newDate.getMonthValue(), newDate.getYear(),
                            newValue, category));

        return Optional.ofNullable(monthlySummary);

    }
}
