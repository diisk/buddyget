package br.dev.diisk.domain.finance.income_transaction;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IIncomeTransactionRepository extends IBaseRepository<IncomeTransaction> {

    Page<IncomeTransaction> findAllBy(Long userId, ListIncomeTransactionsFilter filter, Pageable pageable);

    Set<IncomeTransaction> findByRecurring(Long userId, Long incomeRecurringId);

    Boolean existsByRecurring(Long incomeRecurringId, LocalDateTime startDate, LocalDateTime endDate);

}
