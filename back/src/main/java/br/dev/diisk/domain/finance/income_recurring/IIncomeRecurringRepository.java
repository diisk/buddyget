package br.dev.diisk.domain.finance.income_recurring;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IIncomeRecurringRepository extends IBaseRepository<IncomeRecurring> {

    Page<IncomeRecurring> findAllBy(Long userId, ListIncomeRecurringsFilter filter, Pageable pageable);

    Set<IncomeRecurring> findAllActive();
}
