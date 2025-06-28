package br.dev.diisk.application.finance.income_recurring.cases;

import java.util.Set;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListCurrentIncomeRecurringsCase {

    private final IIncomeRecurringRepository incomeRecurringRepository;

    @Transactional
    public Set<IncomeRecurring> execute() {

        return incomeRecurringRepository.findAllActive();
    }
}
