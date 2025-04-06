package br.dev.diisk.application.cases.income;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.UtilService;
import br.dev.diisk.application.exceptions.date.PeriodOrderException;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.income.ListIncomesFilter;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListIncomesCase {

    private final IIncomeRepository incomeRepository;
    private final UtilService utilService;

    public Page<Income> execute(User user, ListIncomesFilter filter, Pageable pageable) {
        applyDefaultValues(filter);

        if (!filter.getOnlyPending())
            validateIfHasValidDates(filter);

        return incomeRepository.findBy(user.getId(), filter, pageable);
    }

    private void applyDefaultValues(ListIncomesFilter dto) {
        if (dto.getOnlyPending() == null)
            dto.setOnlyPending(false);

        if (!dto.getOnlyPending()) {
            LocalDateTime now = LocalDateTime.now();
            if (dto.getStartReferenceDate() == null)
                dto.setStartReferenceDate(utilService.toReference(now.minusMonths(1)));
        }

    }

    private void validateIfHasValidDates(ListIncomesFilter dto) {
        if (dto.getStartReferenceDate().isAfter(dto.getEndReferenceDate()))
            throw new PeriodOrderException(getClass(),
                    dto.getStartReferenceDate().toString() + " - " + dto.getEndReferenceDate().toString());
    }

}
