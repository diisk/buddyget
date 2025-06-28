package br.dev.diisk.application.finance.income_recurring.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetIncomeRecurringCase {

    private final IIncomeRecurringRepository incomeRecurringRepository;

    @Transactional
    public IncomeRecurring execute(User user, Long incomeRecurringId) {
        IncomeRecurring incomeRecurring = incomeRecurringRepository.findById(incomeRecurringId).orElse(null);
        if (incomeRecurring == null || !incomeRecurring.getUserId().equals(user.getId())) {
            throw new DatabaseValueNotFoundException(getClass(), incomeRecurringId.toString());
        }
        return incomeRecurring;
    }
}
