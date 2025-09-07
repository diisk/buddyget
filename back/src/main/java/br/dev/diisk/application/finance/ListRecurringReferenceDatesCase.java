package br.dev.diisk.application.finance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.finance.Recurring;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListRecurringReferenceDatesCase {

    private final UtilService utilService;

    public List<LocalDateTime> execute(Recurring recurring) {
        List<LocalDateTime> referenceDates = new ArrayList<>();
        LocalDateTime start = utilService.getFirstDayMonthReference(recurring.getStartDate());
        LocalDateTime end = utilService.getLastDayMonthReference(
                recurring.getEndDate() != null ? recurring.getEndDate() : LocalDateTime.now());
        while (!start.isAfter(end)) {
            referenceDates.add(start);
            start = start.plusMonths(1);
        }
        return referenceDates;
    }
}
