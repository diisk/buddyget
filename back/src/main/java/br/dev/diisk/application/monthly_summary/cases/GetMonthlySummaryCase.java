package br.dev.diisk.application.monthly_summary.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.monthly_summary.dtos.GetMonthlySummaryParams;
import br.dev.diisk.domain.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetMonthlySummaryCase {

    private final IMonthlySummaryRepository monthlySummaryRepository;

    @Transactional
    public MonthlySummary execute(User user, GetMonthlySummaryParams params) {
        Long userId = user.getId();
        Integer month = params.getMonth();
        Integer year = params.getYear();
        Long categoryId = params.getCategoryId();
        return monthlySummaryRepository.findBy(userId, month, year, categoryId).orElse(null);
    }
}
