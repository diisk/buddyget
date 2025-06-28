package br.dev.diisk.application.finance.income_recurring.cases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.ListIncomeRecurringsFilter;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListIncomeRecurringsCase {

    private final IIncomeRecurringRepository incomeRecurringRepository;

    @Transactional
    public Page<IncomeRecurring> execute(User user, ListIncomeRecurringsFilter filter, Pageable pageable) {

        return incomeRecurringRepository.findAllBy(user.getId(), filter, pageable);
    }
}
