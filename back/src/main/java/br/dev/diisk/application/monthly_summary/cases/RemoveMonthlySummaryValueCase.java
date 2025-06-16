package br.dev.diisk.application.monthly_summary.cases;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.monthly_summary.dtos.GetMonthlySummaryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoveMonthlySummaryValueCase {

    private final IMonthlySummaryRepository monthlySummaryRepository;
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

        if (monthlySummary == null)
            throw new BusinessException(getClass(), "O resumo mensal alvo está zerado.");

        monthlySummary.removeAmount(value);

        monthlySummaryRepository.save(monthlySummary);
        return monthlySummary;

    }
}
